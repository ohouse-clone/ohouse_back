package com.clone.ohouse.community.domain;


import com.clone.ohouse.account.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.account.domain.user.UserRepository;
import com.clone.ohouse.community.domain.cardcollections.*;
import com.clone.ohouse.community.domain.cardcollections.dto.CardSaveRequestContentDto;
import com.clone.ohouse.community.domain.cardcollections.dto.CardSaveRequestHeaderDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CardServiceTest {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CardContentRepository cardContentRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private CardMediaFileRepository cardMediaFileRepository;

    private Long saveUserId1 = null;

    @BeforeEach
    void setup() {
        User saveUser = userRepository.save(User.builder()
                .email("aa@@bb")
                .password("1234")
                .birthday("00")
                .nickname("jjh")
                .phone("010-0000-0000")
                .name("jjh name")
                .build());

        saveUserId1 = saveUser.getId();
    }

    @AfterEach
    void clean() {
//        cardContentRepository.deleteAll();
//        cardMediaFileRepository.deleteAll();
//        cardRepository.deleteAll();
//        userRepository.deleteAll();
    }

    @Commit
    @Test
    void save() throws Exception {
        //given
        CardSaveRequestHeaderDto headerDto = new CardSaveRequestHeaderDto(HousingType.APARTMENT, HouseStyle.KOREAN_ASIA, Color.WHITE);
        CardSaveRequestContentDto[] contentDtos = {
                new CardSaveRequestContentDto(1, "content1"),
                new CardSaveRequestContentDto(2, "content2"),
                new CardSaveRequestContentDto(3, "content3")};
        MultipartFile[] multipartFiles = {
                null, null, null
        };

        //when
        Long saveId = cardService.save(headerDto, multipartFiles, contentDtos, new SessionUser(User.builder()
                .email("aa@bb")
                .password("1234")
                .birthday("00")
                .nickname("jjh")
                .name("jjh name")
                .phone("010-0000-0000")
                .build()));


        //then
        System.out.println("==================================================================");
        Card findCard = cardRepository.findByIdWithContent(saveId).orElseThrow(() -> new NoSuchElementException("Fail to find card that id is " + saveId));
        System.out.println("==================================================================");
        System.out.println("whatthefuck : " + findCard.getCardContents().size());
        System.out.println("whatthefuck : " + findCard.getId() + " , " + saveId);
        System.out.println("whatthefuck : " + findCard.getColor().name());
        System.out.println("whatthefuck : " + findCard.getCardContents().get(0).getSequence());


        Assertions.assertThat(findCard.getHouseStyle()).isEqualTo(HouseStyle.KOREAN_ASIA);
        Assertions.assertThat(findCard.getColor()).isEqualTo(Color.WHITE);
        Assertions.assertThat(findCard.getHousingType()).isEqualTo(HousingType.APARTMENT);
        Assertions.assertThat(findCard.getCardContents().size()).isEqualTo(3);

        CardContent content1 = cardContentRepository.findById(findCard.getCardContents().get(0).getId()).orElseThrow(() -> new NoSuchElementException("Fail to find cardContent id is " + findCard.getCardContents().get(0).getId()));
        CardContent content2 = cardContentRepository.findById(findCard.getCardContents().get(1).getId()).orElseThrow(() -> new NoSuchElementException("Fail to find cardContent id is " + findCard.getCardContents().get(0).getId()));

        Assertions.assertThat(content1.getContent()).isEqualTo(findCard.getCardContents().get(0).getContent());
        Assertions.assertThat(content2.getContent()).isEqualTo(findCard.getCardContents().get(1).getContent());
    }


}
