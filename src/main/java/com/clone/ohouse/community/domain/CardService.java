package com.clone.ohouse.community.domain;

import com.clone.ohouse.account.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.account.domain.user.UserRepository;
import com.clone.ohouse.community.domain.cardcollections.*;
import com.clone.ohouse.community.domain.cardcollections.dto.*;
import com.clone.ohouse.utility.s3.LocalFileService;
import com.clone.ohouse.utility.s3.S3File;
import com.clone.ohouse.utility.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CardContentRepository cardContentRepository;
    private final CardMediaFileRepository cardMediaFileRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final LocalFileService localFileService;

    public Long save(
            CardSaveRequestHeaderDto headerDto,
            MultipartFile[] multipartFiles,
            CardSaveRequestContentDto[] contentDtos,
            SessionUser sessionUser) throws Exception {
        if(multipartFiles.length != contentDtos.length) throw new IllegalArgumentException("file과 dto의 길이가 맞지 않음");

        User user = userRepository.findByEmail(sessionUser.getEmail());
        Card saveCard = cardRepository.save(new Card(PostType.CARD, headerDto.getHousingType(), headerDto.getHouseStyle(), headerDto.getColor(), user));
        Arrays.sort(contentDtos, (a, b) -> Integer.compare(a.getSequence(), b.getSequence()));

        mergeMediaFileAndContent(saveCard, multipartFiles, contentDtos);


        return saveCard.getId();
    }

    public void update(Long id,
                       CardSaveRequestHeaderDto headerDto,
                       MultipartFile[] multipartFiles,
                       CardSaveRequestContentDto[] contentDtos,
                       SessionUser sessionUser) throws Exception {
        if(multipartFiles.length != contentDtos.length) throw new IllegalArgumentException("file과 dto의 길이가 맞지 않음");

        User user = userRepository.findByEmail(sessionUser.getEmail());
        Card card = cardRepository.findByIdWithContent(id).orElseThrow(() -> new NoSuchElementException("Fail to find, Nothing with id = " + id));

        if (card.getUser().getId() != user.getId()) throw new IllegalAccessException("Access Illegal for auth");

        Arrays.sort(contentDtos, (a, b) -> Integer.compare(a.getSequence(), b.getSequence()));

        card.update(headerDto.getHousingType(), headerDto.getHouseStyle(), headerDto.getColor());
        int newNum = multipartFiles.length;
        int previousContentNum = card.getCardContents().size();

        //case 1
        if (newNum < previousContentNum) {
            int i = 0;
            for (; i < newNum; ++i) {
                MultipartFile multipartFile = multipartFiles[i];
                CardSaveRequestContentDto contentDto = contentDtos[i];
                CardContent content = card.getCardContents().get(i);
                CardMediaFile file = content.getCardMediaFile();

                replaceCardContent(content, contentDto);
                replaceMultiPartFile(file, multipartFile);
            }
            for (i = previousContentNum - 1; i >= newNum; --i) {
                CardContent content = card.getCardContents().get(i);
                CardMediaFile file = content.getCardMediaFile();

                //delete previous
                deleteMultiPartFile(file);
                deleteCardContent(content);
                card.getCardContents().remove(i);
            }
        }
        //case 2
        else if (newNum == previousContentNum) {
            int i = 0;
            for (; i < newNum; ++i) {
                MultipartFile multipartFile = multipartFiles[i];
                CardSaveRequestContentDto contentDto = contentDtos[i];
                CardContent content = card.getCardContents().get(i);
                CardMediaFile file = content.getCardMediaFile();

                replaceCardContent(content, contentDto);
                replaceMultiPartFile(file, multipartFile);
            }
        }
        //case 3
        else if (previousContentNum < newNum) {
            int i = 0;
            for (; i < previousContentNum; ++i) {
                MultipartFile multipartFile = multipartFiles[i];
                CardSaveRequestContentDto contentDto = contentDtos[i];
                CardContent content = card.getCardContents().get(i);
                CardMediaFile file = content.getCardMediaFile();


                replaceCardContent(content, contentDto);
                replaceMultiPartFile(file, multipartFile);
            }

            for (; i < newNum; ++i) {
                MultipartFile multipartFile = multipartFiles[i];
                CardSaveRequestContentDto contentDto = contentDtos[i];
                CardMediaFile saveCardMediaFile = null;


                if (multipartFile != null && s3Service.isRunning()) {
                    S3File s3File = saveMediaFile(multipartFile);
                    saveCardMediaFile = cardMediaFileRepository.save(new CardMediaFile(s3File.getKey(), s3File.getUrl()));
                }

                if (contentDto.getContent() != null) {
                    CardContent saveContent = new CardContent(card, saveCardMediaFile, contentDto.getContent(), contentDto.getSequence());
                    cardContentRepository.save(saveContent);
                    card.getCardContents().add(saveContent);
                }
            }
        }

    }

    public void delete(Long id) {
        Card card = cardRepository.findByIdWithContent(id).orElseThrow(() -> new NoSuchElementException("Fail to delete, Nothing with id = " + id));

        for (CardContent content : card.getCardContents()) {
            if (s3Service.isRunning()) s3Service.delete(content.getCardMediaFile().getKeyName());
            cardMediaFileRepository.delete(content.getCardMediaFile());
            cardContentRepository.delete(content);
        }
        cardRepository.delete(card);
    }

    public CardResponseDto findById(Long id) {
        Card card = cardRepository.findByIdWithContent(id).orElseThrow(() -> new NoSuchElementException("Fail to find, Nothing with id = " + id));

        card.addHit();

        //TODO: Comment 추가되면, Response에도 추가되어야함
        CardResponseDto response = new CardResponseDto(
                card.getId(),
                card.getHit(),
                card.getHousingType(),
                card.getHouseStyle(),
                card.getColor(),
                card.getCreatedDate(),
                card.getModifiedDate());
        response.setContentList(card.getCardContents()
                .stream().map((entity) -> new CardContentResponseDto(entity.getContent(), entity.getCardMediaFile().getS3Url()))
                .collect(Collectors.toCollection(ArrayList<CardContentResponseDto>::new)));

        return response;
    }

    public CardBundleViewResponseDto findBundleViewWithContent(Pageable pageable, CardSearchCondition cardSearchCondition) {
        List<Card> cards = cardRepository.findBundleViewWithContent(pageable, cardSearchCondition);
        Long totalNum = cardRepository.findBundleViewCount(cardSearchCondition);
        Long cardNum = (long) cards.size();
        //TODO: Comment 추가시 수정
        ArrayList<CardPreviewResponseDto> collect = cards.stream().map((entity) ->
                        new CardPreviewResponseDto(
                                entity.getCardContents().get(0).getCardMediaFile().getS3Url(),
                                entity.getHit(),
                                null,
                                entity.getCardContents().get(0).getContent(),
                                null,
                                null))
                .collect(Collectors.toCollection(ArrayList<CardPreviewResponseDto>::new));

        return new CardBundleViewResponseDto(totalNum, cardNum, collect);
    }


    private void mergeMediaFileAndContent(Card card, MultipartFile[] multipartFiles, CardSaveRequestContentDto[] contentDtos) throws Exception {
        int size = multipartFiles.length;

        for (int i = 0; i < size; ++i) {
            MultipartFile multipartFile = multipartFiles[i];
            CardSaveRequestContentDto contentDto = contentDtos[i];
            CardMediaFile saveCardMediaFile = null;

            if (s3Service.isRunning()) {
                S3File s3File = saveMediaFile(multipartFile);
                saveCardMediaFile = cardMediaFileRepository.save(new CardMediaFile(s3File.getKey(), s3File.getUrl()));
            } else {
                log.debug("Disabled AWS Connecting");
                saveCardMediaFile = cardMediaFileRepository.save(new CardMediaFile(null, null));
            }

            CardContent content = new CardContent(card, saveCardMediaFile, contentDto.getContent(), contentDto.getSequence());
            content.registerCard(card);
            cardContentRepository.save(content);

        }
    }


    private S3File saveMediaFile(MultipartFile multipartFile) throws Exception {
        File file = localFileService.createFileBeforeUploadS3(multipartFile, "card_collection");
        S3File s3File = s3Service.upload(file, "card_collection");

        return s3File;
    }

    private void replaceMultiPartFile(CardMediaFile file, MultipartFile replaceFile) throws IOException {
        if (replaceFile == null) return;

        if (file != null && s3Service.isRunning()) {
            log.debug("case1-1 delete previous multiPartFile ");
            s3Service.delete(file.getKeyName());

            //Add new one
            log.debug("case1-1 Add new multiPartFile");
            File newFile = localFileService.createFileBeforeUploadS3(replaceFile, "card_collection");
            S3File s3File = s3Service.upload(newFile, "card_collection");

            //update new one
            log.debug("case1-1 update new multiPartFile in s3");
            file.update(s3File.getKey(), s3File.getUrl());
        }
    }

    private void replaceCardContent(CardContent content, CardSaveRequestContentDto replaceDto) {
        if (replaceDto.getContent() == null) return;
        content.update(null, replaceDto.getContent(), replaceDto.getSequence());

    }

    private void deleteMultiPartFile(CardMediaFile file) {
        if (file != null && s3Service.isRunning()) {
            log.debug("case2 Delete previous multiMediaFile");
            s3Service.delete(file.getKeyName());
            cardMediaFileRepository.delete(file);
        }
    }

    private void deleteCardContent(CardContent content) {
        cardContentRepository.delete(content);
    }

}
