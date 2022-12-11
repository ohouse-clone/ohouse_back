package com.clone.ohouse.community.domain;


import com.clone.ohouse.account.auth.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.account.domain.user.UserRepository;
import com.clone.ohouse.community.domain.cardcollections.*;
import com.clone.ohouse.community.domain.cardcollections.dto.CardBundleViewResponseDto;
import com.clone.ohouse.community.domain.cardcollections.dto.CardResponseDto;
import com.clone.ohouse.community.domain.cardcollections.dto.CardSaveRequestContentDto;
import com.clone.ohouse.community.domain.cardcollections.dto.CardSaveRequestHeaderDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.NoSuchElementException;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CardServiceTest {

    @PersistenceContext
    private EntityManager em;
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
    private User saveUser = null;

    @BeforeEach
    void setup() {
        User user = userRepository.save(User.builder()
                .email("aa@@bb")
                .password("1234")
                .birthday("00")
                .nickname("jjh")
                .phone("010-0000-0000")
                .name("jjh name")
                .build());

        saveUserId1 = user.getId();
        saveUser = user;
    }

    @AfterEach
    void clean() {
        cardContentRepository.deleteAll();
        cardMediaFileRepository.deleteAll();
        cardRepository.deleteAll();
        userRepository.deleteAll();
    }


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
        Long saveId = cardService.save(headerDto, multipartFiles, contentDtos, new SessionUser(saveUser));

        //then
        System.out.println("==================================================================");
        Card findCard = cardRepository.findByIdWithContent(saveId).orElseThrow(() -> new NoSuchElementException("Fail to find card that id is " + saveId));
        System.out.println("==================================================================");


        Assertions.assertThat(findCard.getHouseStyle()).isEqualTo(HouseStyle.KOREAN_ASIA);
        Assertions.assertThat(findCard.getColor()).isEqualTo(Color.WHITE);
        Assertions.assertThat(findCard.getHousingType()).isEqualTo(HousingType.APARTMENT);
        Assertions.assertThat(findCard.getCardContents().size()).isEqualTo(3);

        CardContent content1 = cardContentRepository.findById(findCard.getCardContents().get(0).getId()).orElseThrow(() -> new NoSuchElementException("Fail to find cardContent id is " + findCard.getCardContents().get(0).getId()));
        CardContent content2 = cardContentRepository.findById(findCard.getCardContents().get(1).getId()).orElseThrow(() -> new NoSuchElementException("Fail to find cardContent id is " + findCard.getCardContents().get(0).getId()));

        Assertions.assertThat(content1.getContent()).isEqualTo(findCard.getCardContents().get(0).getContent());
        Assertions.assertThat(content2.getContent()).isEqualTo(findCard.getCardContents().get(1).getContent());
    }

    @Test
    void updateNumberIsSameContent() throws Exception {
        //given
        CardSaveRequestHeaderDto headerDto = new CardSaveRequestHeaderDto(HousingType.APARTMENT, HouseStyle.KOREAN_ASIA, Color.WHITE);
        CardSaveRequestContentDto[] contentDtos = {
                new CardSaveRequestContentDto(1, "content1"),
                new CardSaveRequestContentDto(2, "content2"),
                new CardSaveRequestContentDto(3, "content3")};
        MultipartFile[] multipartFiles = {
                null, null, null
        };
        Long saveId = cardService.save(headerDto, multipartFiles, contentDtos, new SessionUser(saveUser));

        CardSaveRequestHeaderDto updateHeaderDto = new CardSaveRequestHeaderDto(HousingType.COMMERCIAL, null, null);
        CardSaveRequestContentDto[] updateContentDtos = {
                new CardSaveRequestContentDto(3, "content6"),
                new CardSaveRequestContentDto(1, "content9"),
                new CardSaveRequestContentDto(2, "content8")
        };
        MultipartFile[] updateMultipartFiles = {null, null, null};

        //when
        System.out.println("==================================================================");
        cardService.update(saveId, updateHeaderDto, updateMultipartFiles, updateContentDtos, new SessionUser(saveUser));
        System.out.println("==================================================================");

        //then
        Card updatedCard = cardRepository.findByIdWithContent(saveId).orElseThrow(() -> new NoSuchElementException("없는 ID " + saveId));
        Assertions.assertThat(updatedCard.getHousingType()).isEqualTo(HousingType.COMMERCIAL);
        Assertions.assertThat(updatedCard.getHouseStyle()).isEqualTo(HouseStyle.KOREAN_ASIA);
        Assertions.assertThat(updatedCard.getColor()).isEqualTo(Color.WHITE);
        Assertions.assertThat(updatedCard.getCardContents().size()).isEqualTo(3);
        Assertions.assertThat(updatedCard.getCardContents().get(0).getContent()).isEqualTo("content9");
        Assertions.assertThat(updatedCard.getCardContents().get(1).getContent()).isEqualTo("content8");
        Assertions.assertThat(updatedCard.getCardContents().get(2).getContent()).isEqualTo("content6");
    }

    @Test
    void updateNumberBiggerThanPrevious() throws Exception {
        //given
        CardSaveRequestHeaderDto headerDto = new CardSaveRequestHeaderDto(HousingType.APARTMENT, HouseStyle.KOREAN_ASIA, Color.WHITE);
        CardSaveRequestContentDto[] contentDtos = {
                new CardSaveRequestContentDto(1, "content1"),
                new CardSaveRequestContentDto(2, "content2"),
                new CardSaveRequestContentDto(3, "content3")};
        MultipartFile[] multipartFiles = {
                null, null, null
        };
        Long saveId = cardService.save(headerDto, multipartFiles, contentDtos, new SessionUser(saveUser));

        CardSaveRequestHeaderDto updateHeaderDto = new CardSaveRequestHeaderDto(HousingType.COMMERCIAL, null, null);
        CardSaveRequestContentDto[] updateContentDtos = {
                new CardSaveRequestContentDto(3, "bigger1"),
                new CardSaveRequestContentDto(1, "bigger2"),
                new CardSaveRequestContentDto(2, "bigger3"),
                new CardSaveRequestContentDto(6, "bigger4"),
                new CardSaveRequestContentDto(5, "bigger5"),
                new CardSaveRequestContentDto(4, "bigger6"),
        };
        MultipartFile[] updateMultipartFiles = {null, null, null, null, null, null};

        //when
        System.out.println("==================================================================");
        cardService.update(saveId, updateHeaderDto, updateMultipartFiles, updateContentDtos, new SessionUser(saveUser));
        System.out.println("==================================================================");

        //then
        Card updatedCard = cardRepository.findByIdWithContent(saveId).orElseThrow(() -> new NoSuchElementException("없는 ID " + saveId));
        Assertions.assertThat(updatedCard.getHousingType()).isEqualTo(HousingType.COMMERCIAL);
        Assertions.assertThat(updatedCard.getHouseStyle()).isEqualTo(HouseStyle.KOREAN_ASIA);
        Assertions.assertThat(updatedCard.getColor()).isEqualTo(Color.WHITE);
        Assertions.assertThat(updatedCard.getCardContents().size()).isEqualTo(6);
        Assertions.assertThat(updatedCard.getCardContents().get(0).getContent()).isEqualTo("bigger2");
        Assertions.assertThat(updatedCard.getCardContents().get(1).getContent()).isEqualTo("bigger3");
        Assertions.assertThat(updatedCard.getCardContents().get(2).getContent()).isEqualTo("bigger1");
    }

    @Test
    void updateNumberSmallerThanPrevious() throws Exception {
        //given
        CardSaveRequestHeaderDto headerDto = new CardSaveRequestHeaderDto(HousingType.APARTMENT, HouseStyle.KOREAN_ASIA, Color.WHITE);
        CardSaveRequestContentDto[] contentDtos = {
                new CardSaveRequestContentDto(1, "content1"),
                new CardSaveRequestContentDto(2, "content2"),
                new CardSaveRequestContentDto(3, "content3"),
                new CardSaveRequestContentDto(4, "content4")};
        MultipartFile[] multipartFiles = {
                null, null, null, null
        };
        Long saveId = cardService.save(headerDto, multipartFiles, contentDtos, new SessionUser(saveUser));

        CardSaveRequestHeaderDto updateHeaderDto = new CardSaveRequestHeaderDto(HousingType.COMMERCIAL, null, null);
        CardSaveRequestContentDto[] updateContentDtos = {
                new CardSaveRequestContentDto(2, "smaller2"),
                new CardSaveRequestContentDto(1, "smaller1"),

        };
        MultipartFile[] updateMultipartFiles = {null, null};

        //when
        System.out.println("==================================================================");
        cardService.update(saveId, updateHeaderDto, updateMultipartFiles, updateContentDtos, new SessionUser(saveUser));
        System.out.println("==================================================================");

        //then
        Card updatedCard = cardRepository.findByIdWithContent(saveId).orElseThrow(() -> new NoSuchElementException("없는 ID " + saveId));
        Assertions.assertThat(updatedCard.getHousingType()).isEqualTo(HousingType.COMMERCIAL);
        Assertions.assertThat(updatedCard.getHouseStyle()).isEqualTo(HouseStyle.KOREAN_ASIA);
        Assertions.assertThat(updatedCard.getColor()).isEqualTo(Color.WHITE);
        Assertions.assertThat(updatedCard.getCardContents().size()).isEqualTo(2);
        Assertions.assertThat(updatedCard.getCardContents().get(0).getContent()).isEqualTo("smaller1");
        Assertions.assertThat(updatedCard.getCardContents().get(1).getContent()).isEqualTo("smaller2");
    }

    @Test
    void delete() throws Exception{
        //given
        CardSaveRequestHeaderDto headerDto = new CardSaveRequestHeaderDto(HousingType.APARTMENT, HouseStyle.KOREAN_ASIA, Color.WHITE);
        CardSaveRequestContentDto[] contentDtos = {
                new CardSaveRequestContentDto(1, "content1"),
                new CardSaveRequestContentDto(2, "content2"),
                new CardSaveRequestContentDto(3, "content3")};
        MultipartFile[] multipartFiles = {
                null, null, null
        };
        Long saveId = cardService.save(headerDto, multipartFiles, contentDtos, new SessionUser(saveUser));

        //when
        cardService.delete(saveId);


        //then
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class,
                () -> cardRepository.findById(saveId).orElseThrow(()->new NoSuchElementException()));
        Assertions.assertThat(cardRepository.count()).isEqualTo(0);

    }

    @Test
    void findById() throws Exception{
        //given
        CardSaveRequestHeaderDto headerDto = new CardSaveRequestHeaderDto(HousingType.APARTMENT, HouseStyle.KOREAN_ASIA, Color.WHITE);
        CardSaveRequestContentDto[] contentDtos = {
                new CardSaveRequestContentDto(1, "content1"),
                new CardSaveRequestContentDto(2, "content2"),
                new CardSaveRequestContentDto(3, "content3")};
        MultipartFile[] multipartFiles = {
                null, null, null
        };
        Long saveId = cardService.save(headerDto, multipartFiles, contentDtos, new SessionUser(saveUser));

        //when
        CardResponseDto responseDto = cardService.findById(saveId);

        //then
        Assertions.assertThat(responseDto.getId()).isEqualTo(saveId);
        Assertions.assertThat(responseDto.getHousingType()).isEqualTo(HousingType.APARTMENT);
        Assertions.assertThat(responseDto.getHouseStyle()).isEqualTo(HouseStyle.KOREAN_ASIA);
        Assertions.assertThat(responseDto.getColor()).isEqualTo(Color.WHITE);
        Assertions.assertThat(responseDto.getContentNum()).isEqualTo(3);
        Assertions.assertThat(responseDto.getHit()).isEqualTo(1);
        Assertions.assertThat(responseDto.getContentList().get(0).getContent()).isEqualTo("content1");
        Assertions.assertThat(responseDto.getContentList().get(1).getContent()).isEqualTo("content2");
        Assertions.assertThat(responseDto.getContentList().get(2).getContent()).isEqualTo("content3");
        Assertions.assertThat(responseDto.getContentList().get(0).getFileUrl()).isEqualTo(null);
    }

    @Test
    void findBundleViewWithContent() throws Exception {
        //given
        CardSaveRequestHeaderDto headerDto1 = new CardSaveRequestHeaderDto(HousingType.APARTMENT, HouseStyle.KOREAN_ASIA, Color.WHITE);
        CardSaveRequestHeaderDto headerDto2 = new CardSaveRequestHeaderDto(HousingType.APARTMENT, HouseStyle.KOREAN_ASIA, Color.BLACK);
        CardSaveRequestHeaderDto headerDto3 = new CardSaveRequestHeaderDto(HousingType.APARTMENT, HouseStyle.MODERN, Color.BLACK);
        CardSaveRequestHeaderDto headerDto4 = new CardSaveRequestHeaderDto(HousingType.COMMERCIAL, HouseStyle.KOREAN_ASIA, Color.BLACK);
        CardSaveRequestHeaderDto headerDto5 = new CardSaveRequestHeaderDto(HousingType.COMMERCIAL, HouseStyle.ROMANTIC, Color.BLACK);
        CardSaveRequestHeaderDto headerDto6 = new CardSaveRequestHeaderDto(HousingType.COMMERCIAL, HouseStyle.ROMANTIC, Color.RED);
        CardSaveRequestHeaderDto headerDto7 = new CardSaveRequestHeaderDto(HousingType.COMMERCIAL, HouseStyle.ROMANTIC, Color.RED);
        CardSaveRequestHeaderDto headerDto8 = new CardSaveRequestHeaderDto(HousingType.SINGLE_HOUSE, HouseStyle.NORTH_EUROPE, Color.BLUE);
        CardSaveRequestContentDto[] contentDtos1 = {new CardSaveRequestContentDto(1, "content1-1"), new CardSaveRequestContentDto(2, "content1-2"), new CardSaveRequestContentDto(3, "content1-3")};
        CardSaveRequestContentDto[] contentDtos2 = {new CardSaveRequestContentDto(1, "content2-1"), new CardSaveRequestContentDto(2, "content2-2"), new CardSaveRequestContentDto(3, "content2-3")};
        CardSaveRequestContentDto[] contentDtos3 = {new CardSaveRequestContentDto(1, "content3-1"), new CardSaveRequestContentDto(2, "content3-2"), new CardSaveRequestContentDto(3, "content3-3")};
        CardSaveRequestContentDto[] contentDtos4 = {new CardSaveRequestContentDto(1, "content4-1"), new CardSaveRequestContentDto(2, "content4-2"), new CardSaveRequestContentDto(3, "content4-3")};
        CardSaveRequestContentDto[] contentDtos5 = {new CardSaveRequestContentDto(1, "content5-1"), new CardSaveRequestContentDto(2, "content5-2"), new CardSaveRequestContentDto(3, "content5-3")};
        CardSaveRequestContentDto[] contentDtos6 = {new CardSaveRequestContentDto(1, "content6-1"), new CardSaveRequestContentDto(2, "content6-2"), new CardSaveRequestContentDto(3, "content6-3")};
        CardSaveRequestContentDto[] contentDtos7 = {new CardSaveRequestContentDto(1, "content7-1"), new CardSaveRequestContentDto(2, "content7-2"), new CardSaveRequestContentDto(3, "content7-3")};
        CardSaveRequestContentDto[] contentDtos8 = {new CardSaveRequestContentDto(1, "content8-1"), new CardSaveRequestContentDto(2, "content8-2"), new CardSaveRequestContentDto(3, "content8-3")};
        MultipartFile[] multipartFiles1 = {null, null, null};
        MultipartFile[] multipartFiles2 = {null, null, null};
        MultipartFile[] multipartFiles3 = {null, null, null};
        MultipartFile[] multipartFiles4 = {null, null, null};
        MultipartFile[] multipartFiles5 = {null, null, null};
        MultipartFile[] multipartFiles6 = {null, null, null};
        MultipartFile[] multipartFiles7 = {null, null, null};
        MultipartFile[] multipartFiles8 = {null, null, null};

        Long saveId1 = cardService.save(headerDto1, multipartFiles1, contentDtos1, new SessionUser(saveUser));
        Long saveId2 = cardService.save(headerDto2, multipartFiles2, contentDtos2, new SessionUser(saveUser));
        Long saveId3 = cardService.save(headerDto3, multipartFiles3, contentDtos3, new SessionUser(saveUser));
        Long saveId4 = cardService.save(headerDto4, multipartFiles4, contentDtos4, new SessionUser(saveUser));
        Long saveId5 = cardService.save(headerDto5, multipartFiles5, contentDtos5, new SessionUser(saveUser));
        Long saveId6 = cardService.save(headerDto6, multipartFiles6, contentDtos6, new SessionUser(saveUser));
        Long saveId7 = cardService.save(headerDto7, multipartFiles7, contentDtos7, new SessionUser(saveUser));
        Long saveId8 = cardService.save(headerDto8, multipartFiles8, contentDtos8, new SessionUser(saveUser));

        Pageable pageable = PageRequest.of(0, 3);
        Pageable pageable2 = PageRequest.of(0, 2);
        Pageable pageable3 = PageRequest.of(1, 1);
        CardSearchCondition condition0 = new CardSearchCondition(null, null, null, null);
        CardSearchCondition condition1 = new CardSearchCondition(HousingType.APARTMENT, null, null, null);
        CardSearchCondition condition2 = new CardSearchCondition(HousingType.COMMERCIAL, HouseStyle.ROMANTIC, null, null);
        CardSearchCondition condition3 = new CardSearchCondition(HousingType.COMMERCIAL, HouseStyle.ROMANTIC, Color.RED, null);

        //when 0
        CardBundleViewResponseDto bundleViewResponseDto0 = cardService.findBundleViewWithContent(pageable, condition0);
        //then 0
        Assertions.assertThat(bundleViewResponseDto0.getTotalNum()).isEqualTo(8);
        Assertions.assertThat(bundleViewResponseDto0.getCardNum()).isEqualTo(3);
        Assertions.assertThat(bundleViewResponseDto0.getPreviewCards().get(0).getPreviewContent()).isEqualTo("content1-1");


        //when 1
        CardBundleViewResponseDto bundleViewResponseDto1 = cardService.findBundleViewWithContent(pageable, condition1);
        //then 1
        Assertions.assertThat(bundleViewResponseDto1.getTotalNum()).isEqualTo(3);
        Assertions.assertThat(bundleViewResponseDto1.getCardNum()).isEqualTo(3);
        Assertions.assertThat(bundleViewResponseDto1.getPreviewCards().get(0).getPreviewContent()).isEqualTo("content1-1");
        Assertions.assertThat(bundleViewResponseDto1.getPreviewCards().get(0).getPreviewImageUrl()).isEqualTo(null);
        Assertions.assertThat(bundleViewResponseDto1.getPreviewCards().get(2).getPreviewContent()).isEqualTo("content3-1");

        //when 2
        CardBundleViewResponseDto bundleViewResponseDto2 = cardService.findBundleViewWithContent(pageable2, condition2);
        //then 2
        Assertions.assertThat(bundleViewResponseDto2.getTotalNum()).isEqualTo(3);
        Assertions.assertThat(bundleViewResponseDto2.getCardNum()).isEqualTo(2);
        Assertions.assertThat(bundleViewResponseDto2.getPreviewCards().get(0).getPreviewContent()).isEqualTo("content5-1");
        Assertions.assertThat(bundleViewResponseDto2.getPreviewCards().get(0).getPreviewImageUrl()).isEqualTo(null);
        Assertions.assertThat(bundleViewResponseDto2.getPreviewCards().get(1).getPreviewContent()).isEqualTo("content6-1");

        //when 3
        CardBundleViewResponseDto bundleViewResponseDto3 = cardService.findBundleViewWithContent(pageable3, condition3);
        //then 3
        Assertions.assertThat(bundleViewResponseDto3.getTotalNum()).isEqualTo(2);
        Assertions.assertThat(bundleViewResponseDto3.getCardNum()).isEqualTo(1);
        Assertions.assertThat(bundleViewResponseDto3.getPreviewCards().get(0).getPreviewContent()).isEqualTo("content7-1");
    }


}
