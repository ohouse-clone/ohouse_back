package com.clone.ohouse.community;

import com.clone.ohouse.account.auth.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.account.domain.user.UserRepository;
import com.clone.ohouse.community.domain.CardService;
import com.clone.ohouse.community.domain.cardcollections.*;
import com.clone.ohouse.community.domain.cardcollections.dto.CardBundleViewResponseDto;
import com.clone.ohouse.community.domain.cardcollections.dto.CardSaveRequestContentDto;
import com.clone.ohouse.community.domain.cardcollections.dto.CardSaveRequestHeaderDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.startup.HomesUserDatabase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CardApiControllerTest {
    @LocalServerPort
    private int port;
    @Autowired

    private MockMvc mvc;
//    @Autowired private WebApplicationContext context;
    private final String mappingUrl = "/community/api/v1/card_collections";

    @Autowired private CardService cardService;
    @Autowired private CardRepository cardRepository;
    @Autowired private CardContentRepository cardContentRepository;
    @Autowired private CardMediaFileRepository cardMediaFileRepository;
    @Autowired private UserRepository userRepository;

    private User saveUser = null;
    private Long saveCardId1 = null;

    @Autowired private ObjectMapper objectMapper;
    @BeforeEach
    void setup() throws Exception {
//        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        setupUser();

    }
    @Transactional
    void setupUser(){
        saveUser = userRepository.save(
                User.builder()
                        .email("jjh@bb.com")
                        .name("jjh")
                        .phone("010-0000-0000")
                        .nickname("j3")
                        .password("0x00")
                        .build());
    }

    @Transactional
    void setupCard1() throws Exception{
        CardSaveRequestHeaderDto headerDto = new CardSaveRequestHeaderDto(HousingType.OFFICE, HouseStyle.NORTH_EUROPE, Color.BLUE);
        CardSaveRequestContentDto[] contentDtos = {
                new CardSaveRequestContentDto(1, "con1"),
                new CardSaveRequestContentDto(2, "con2"),
                new CardSaveRequestContentDto(3, "con3"),
                new CardSaveRequestContentDto(4, "con4")};
        MultipartFile[] multipartFiles = {null, null, null, null};

        saveCardId1 = cardService.save(headerDto, multipartFiles, contentDtos, new SessionUser(saveUser));
    }


    @AfterEach
    void clean(){
        cardContentRepository.deleteAll();
        cardMediaFileRepository.deleteAll();
        cardRepository.deleteAll();
        userRepository.deleteAll();
    }
    @Test
    void save() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl;
        String imageFilePath = "src/test/resources";
        CardSaveRequestHeaderDto headerDto = new CardSaveRequestHeaderDto(HousingType.OFFICE, HouseStyle.NORTH_EUROPE, Color.BLUE);
        CardSaveRequestContentDto[] contentDtos = {
                new CardSaveRequestContentDto(1, "con1"),
                new CardSaveRequestContentDto(2, "con2"),
                new CardSaveRequestContentDto(3, "con3")};
        MockMultipartFile[] multipartFiles = {
                new MockMultipartFile("files", "dog.jpeg", "image/jpeg", new FileInputStream(imageFilePath +"/images/dog.jpeg")),
                new MockMultipartFile("files", "dog2.jpeg", "image/jpeg", new FileInputStream(imageFilePath +"/images/dog2.jpeg")),
                new MockMultipartFile("files", "iphone.jpg", "image/jpg", new FileInputStream(imageFilePath +"/images/iphone.jpg"))};
        MockMultipartFile json = new MockMultipartFile("contents", "jsondata", "application/json",
                ("[" +
                        objectMapper.writeValueAsString(contentDtos[0])
                        + "," + objectMapper.writeValueAsString(contentDtos[1])
                        + "," + objectMapper.writeValueAsString(contentDtos[2])
                +"]").getBytes(StandardCharsets.UTF_8));

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.multipart(url)
                //Not Working
//                        .part(new MockPart("header", new ObjectMapper().writeValueAsString(headerDto).getBytes(StandardCharsets.UTF_8)))
                        .file(new MockMultipartFile("header", "json", "application/json", objectMapper.writeValueAsString(headerDto).getBytes(StandardCharsets.UTF_8)))
                        .file(multipartFiles[0])
                        .file(multipartFiles[1])
                        .file(multipartFiles[2])
                        .file(json)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        ).andDo(print());

        //then
        perform.andExpect(MockMvcResultMatchers.status().isCreated());
        Long saveId = Long.valueOf(perform.andReturn().getResponse().getContentAsString());
        Card saveCard = cardRepository.findByIdWithContent(saveId).orElseThrow(() -> new NoSuchElementException("해당 ID Card가 없음 : " + saveId));
        Assertions.assertThat(saveCard.getHousingType()).isEqualTo(HousingType.OFFICE);
        Assertions.assertThat(saveCard.getCardContents().size()).isEqualTo(3);
        Assertions.assertThat(saveCard.getCardContents().get(2).getContent()).isEqualTo("con3");
    }

    @Test
    void saveWithNotMatchedContentNum() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl;
        String imageFilePath = "src/test/resources";
        CardSaveRequestHeaderDto headerDto = new CardSaveRequestHeaderDto(HousingType.OFFICE, HouseStyle.NORTH_EUROPE, Color.BLUE);
        CardSaveRequestContentDto[] contentDtos = {
                new CardSaveRequestContentDto(1, "con1"),
                new CardSaveRequestContentDto(2, "con2")};
        MockMultipartFile[] multipartFiles = {
                new MockMultipartFile("files", "dog.jpeg", "image/jpeg", new FileInputStream(imageFilePath +"/images/dog.jpeg")),
                new MockMultipartFile("files", "dog2.jpeg", "image/jpeg", new FileInputStream(imageFilePath +"/images/dog2.jpeg")),
                new MockMultipartFile("files", "iphone.jpg", "image/jpg", new FileInputStream(imageFilePath +"/images/iphone.jpg"))};
        MockMultipartFile json = new MockMultipartFile("contents", "jsondata", "application/json",
                ("[" +
                        objectMapper.writeValueAsString(contentDtos[0])
                        + "," + objectMapper.writeValueAsString(contentDtos[1])
                        +"]").getBytes(StandardCharsets.UTF_8));

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.multipart(url)
                //Not Working
//                .part(new MockPart("header", new ObjectMapper().writeValueAsString(headerDto).getBytes(StandardCharsets.UTF_8)))
                .file(new MockMultipartFile("header", "json", "application/json", objectMapper.writeValueAsString(headerDto).getBytes(StandardCharsets.UTF_8)))
                .file(multipartFiles[0])
                .file(multipartFiles[1])
                .file(multipartFiles[2])
                .file(json)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        ).andDo(print());

        //then
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateNumberIsSameContent() throws Exception {
        //given

        //saving
        CardSaveRequestHeaderDto headerDto = new CardSaveRequestHeaderDto(HousingType.APARTMENT, HouseStyle.KOREAN_ASIA, Color.WHITE);
        CardSaveRequestContentDto[] contentDtos = {
                new CardSaveRequestContentDto(1, "content1"),
                new CardSaveRequestContentDto(2, "content2"),
                new CardSaveRequestContentDto(3, "content3")};
        MultipartFile[] multipartFiles = {
                null, null, null
        };
        Long saveId = cardService.save(headerDto, multipartFiles, contentDtos, new SessionUser(saveUser));


        //new update
        String url = "http://localhost:" + port + mappingUrl;

        String imageFilePath = "src/test/resources";
        CardSaveRequestHeaderDto newHeaderDto = new CardSaveRequestHeaderDto(HousingType.COMMERCIAL, HouseStyle.MODERN, Color.BLUE);
        CardSaveRequestContentDto[] newContentDtos = {
                new CardSaveRequestContentDto(1, "newContent1"),
                new CardSaveRequestContentDto(2, "newContent2"),
                new CardSaveRequestContentDto(3, "newContent3")};
        MockMultipartFile[] newMultipartFiles = {
                new MockMultipartFile("files", "dog.jpeg", "image/jpeg", new FileInputStream(imageFilePath +"/images/dog.jpeg")),
                new MockMultipartFile("files", "dog2.jpeg", "image/jpeg", new FileInputStream(imageFilePath +"/images/dog2.jpeg")),
                new MockMultipartFile("files", "iphone.jpg", "image/jpg", new FileInputStream(imageFilePath +"/images/iphone.jpg"))};
        MockMultipartFile json = new MockMultipartFile("contents", "jsondata", "application/json",
                ("[" +
                        objectMapper.writeValueAsString(newContentDtos[0])
                        + "," + objectMapper.writeValueAsString(newContentDtos[1])
                        + "," + objectMapper.writeValueAsString(newContentDtos[2])
                        +"]").getBytes(StandardCharsets.UTF_8));

        //when
        mvc.perform(MockMvcRequestBuilders.multipart(url + "/" + saveId)
                .file(new MockMultipartFile("header", "headerDto", "application/json", objectMapper.writeValueAsString(newHeaderDto).getBytes(StandardCharsets.UTF_8)))
                .file(newMultipartFiles[0])
                        .file(newMultipartFiles[1])
                        .file(newMultipartFiles[2])
                        .file(json)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        ).andDo(print());


        //then
        Card findCard = cardRepository.findByIdWithContent(saveId).orElseThrow(() -> new NoSuchElementException("해당 ID Card가 없음 : " + saveId));
        Assertions.assertThat(findCard.getHousingType()).isEqualTo(HousingType.COMMERCIAL);
        Assertions.assertThat(findCard.getHouseStyle()).isEqualTo(HouseStyle.MODERN);
        Assertions.assertThat(findCard.getColor()).isEqualTo(Color.BLUE);
        Assertions.assertThat(findCard.getCardContents().size()).isEqualTo(3);
        Assertions.assertThat(findCard.getCardContents().get(0).getContent()).isEqualTo("newContent1");
        Assertions.assertThat(findCard.getCardContents().get(1).getContent()).isEqualTo("newContent2");
        Assertions.assertThat(findCard.getCardContents().get(2).getContent()).isEqualTo("newContent3");
    }


    @Test
    void updateNumberBiggerThanPrevious() throws Exception {
        //given

        //saving
        CardSaveRequestHeaderDto headerDto = new CardSaveRequestHeaderDto(HousingType.APARTMENT, HouseStyle.KOREAN_ASIA, Color.WHITE);
        CardSaveRequestContentDto[] contentDtos = {
                new CardSaveRequestContentDto(1, "content1"),
                new CardSaveRequestContentDto(2, "content2"),
                new CardSaveRequestContentDto(3, "content3")};
        MultipartFile[] multipartFiles = {
                null, null, null
        };
        Long saveId = cardService.save(headerDto, multipartFiles, contentDtos, new SessionUser(saveUser));


        //new update
        String url = "http://localhost:" + port + mappingUrl;

        String imageFilePath = "src/test/resources";
        CardSaveRequestHeaderDto newHeaderDto = new CardSaveRequestHeaderDto(HousingType.COMMERCIAL, HouseStyle.MODERN, Color.BLUE);
        CardSaveRequestContentDto[] newContentDtos = {
                new CardSaveRequestContentDto(1, "newContent1"),
                new CardSaveRequestContentDto(2, "newContent2"),
                new CardSaveRequestContentDto(3, "newContent3"),
                new CardSaveRequestContentDto(4, "newContent4"),
                new CardSaveRequestContentDto(5, "newContent5"),
                new CardSaveRequestContentDto(6, "newContent6")
        };
        MockMultipartFile[] newMultipartFiles = {
                new MockMultipartFile("files", "dog.jpeg", "image/jpeg", new FileInputStream(imageFilePath +"/images/dog.jpeg")),
                new MockMultipartFile("files", "dog2.jpeg", "image/jpeg", new FileInputStream(imageFilePath +"/images/dog2.jpeg")),
                new MockMultipartFile("files", "iphone1.jpg", "image/jpg", new FileInputStream(imageFilePath +"/images/iphone.jpg")),
                new MockMultipartFile("files", "iphone2.jpg", "image/jpg", new FileInputStream(imageFilePath +"/images/iphone.jpg")),
                new MockMultipartFile("files", "iphone3.jpg", "image/jpg", new FileInputStream(imageFilePath +"/images/iphone.jpg")),
                new MockMultipartFile("files", "iphone4.jpg", "image/jpg", new FileInputStream(imageFilePath +"/images/iphone.jpg"))
        };
        MockMultipartFile json = new MockMultipartFile("contents", "jsondata", "application/json",
                ("[" +
                        objectMapper.writeValueAsString(newContentDtos[0])
                        + "," + objectMapper.writeValueAsString(newContentDtos[1])
                        + "," + objectMapper.writeValueAsString(newContentDtos[2])
                        + "," + objectMapper.writeValueAsString(newContentDtos[3])
                        + "," + objectMapper.writeValueAsString(newContentDtos[4])
                        + "," + objectMapper.writeValueAsString(newContentDtos[5])
                        +"]").getBytes(StandardCharsets.UTF_8));

        //when
        mvc.perform(MockMvcRequestBuilders.multipart(url + "/" + saveId)
                .file(new MockMultipartFile("header", "headerDto", "application/json", objectMapper.writeValueAsString(newHeaderDto).getBytes(StandardCharsets.UTF_8)))
                .file(newMultipartFiles[0])
                .file(newMultipartFiles[1])
                .file(newMultipartFiles[2])
                .file(newMultipartFiles[3])
                .file(newMultipartFiles[4])
                .file(newMultipartFiles[5])
                .file(json)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        ).andDo(print());


        //then
        Card findCard = cardRepository.findByIdWithContent(saveId).orElseThrow(() -> new NoSuchElementException("해당 ID Card가 없음 : " + saveId));
        Assertions.assertThat(findCard.getHousingType()).isEqualTo(HousingType.COMMERCIAL);
        Assertions.assertThat(findCard.getHouseStyle()).isEqualTo(HouseStyle.MODERN);
        Assertions.assertThat(findCard.getColor()).isEqualTo(Color.BLUE);
        Assertions.assertThat(findCard.getCardContents().size()).isEqualTo(6);
        Assertions.assertThat(findCard.getCardContents().get(0).getContent()).isEqualTo("newContent1");
        Assertions.assertThat(findCard.getCardContents().get(1).getContent()).isEqualTo("newContent2");
        Assertions.assertThat(findCard.getCardContents().get(2).getContent()).isEqualTo("newContent3");
        Assertions.assertThat(findCard.getCardContents().get(3).getContent()).isEqualTo("newContent4");
        Assertions.assertThat(findCard.getCardContents().get(4).getContent()).isEqualTo("newContent5");
        Assertions.assertThat(findCard.getCardContents().get(5).getContent()).isEqualTo("newContent6");
    }



    @Test
    void updateNumberSmallerThanPrevious() throws Exception {
        //given

        //saving
        CardSaveRequestHeaderDto headerDto = new CardSaveRequestHeaderDto(HousingType.APARTMENT, HouseStyle.KOREAN_ASIA, Color.WHITE);
        CardSaveRequestContentDto[] contentDtos = {
                new CardSaveRequestContentDto(1, "content1"),
                new CardSaveRequestContentDto(2, "content2"),
                new CardSaveRequestContentDto(3, "content3")};
        MultipartFile[] multipartFiles = {
                null, null, null
        };
        Long saveId = cardService.save(headerDto, multipartFiles, contentDtos, new SessionUser(saveUser));


        //new update
        String url = "http://localhost:" + port + mappingUrl;

        String imageFilePath = "src/test/resources";
        CardSaveRequestHeaderDto newHeaderDto = new CardSaveRequestHeaderDto(HousingType.COMMERCIAL, HouseStyle.MODERN, Color.BLUE);
        CardSaveRequestContentDto[] newContentDtos = {
                new CardSaveRequestContentDto(1, "newContent1"),
                new CardSaveRequestContentDto(2, "newContent2")
        };
        MockMultipartFile[] newMultipartFiles = {
                new MockMultipartFile("files", "dog.jpeg", "image/jpeg", new FileInputStream(imageFilePath +"/images/dog.jpeg")),
                new MockMultipartFile("files", "dog2.jpeg", "image/jpeg", new FileInputStream(imageFilePath +"/images/dog2.jpeg"))
        };
        MockMultipartFile json = new MockMultipartFile("contents", "jsondata", "application/json",
                ("[" +
                        objectMapper.writeValueAsString(newContentDtos[0])
                        + "," + objectMapper.writeValueAsString(newContentDtos[1])
                        +"]").getBytes(StandardCharsets.UTF_8));

        //when
        mvc.perform(MockMvcRequestBuilders.multipart(url + "/" + saveId)
                .file(new MockMultipartFile("header", "headerDto", "application/json", objectMapper.writeValueAsString(newHeaderDto).getBytes(StandardCharsets.UTF_8)))
                .file(newMultipartFiles[0])
                .file(newMultipartFiles[1])
                .file(json)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        ).andDo(print());


        //then
        Card findCard = cardRepository.findByIdWithContent(saveId).orElseThrow(() -> new NoSuchElementException("해당 ID Card가 없음 : " + saveId));
        Assertions.assertThat(findCard.getHousingType()).isEqualTo(HousingType.COMMERCIAL);
        Assertions.assertThat(findCard.getHouseStyle()).isEqualTo(HouseStyle.MODERN);
        Assertions.assertThat(findCard.getColor()).isEqualTo(Color.BLUE);
        Assertions.assertThat(findCard.getCardContents().size()).isEqualTo(2);
        Assertions.assertThat(findCard.getCardContents().get(0).getContent()).isEqualTo("newContent1");
        Assertions.assertThat(findCard.getCardContents().get(1).getContent()).isEqualTo("newContent2");
    }

    @Test
    void delete() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl;
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
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.delete(url + "/" + saveId));


        //then
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class,
                () -> cardRepository.findById(saveId).orElseThrow(()-> new NoSuchElementException("해당 ID가 존재하지 않습니다.")));

    }


    @Test
    void findById() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl;
        setupCard1();

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(url + "/" + saveCardId1));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(saveCardId1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.housingType").value(HousingType.OFFICE.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.houseStyle").value(HouseStyle.NORTH_EUROPE.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(Color.BLUE.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contentNum").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contentList[0].content").value("con1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contentList[3].content").value("con4"));
    }

    @Test
    void findBundleView() throws Exception {
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


        String url = "http://localhost:" + port + mappingUrl + "/cards";

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .queryParam("housingtype", "COMMERCIAL")
                        .queryParam("page", "0")
                        .queryParam("size", "3"))
                .andDo(print());

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        perform.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.totalNum").value(4));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.cardNum").value(3));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.previewCards[0].previewContent").value("content4-1"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.previewCards[1].previewContent").value("content5-1"));
        perform.andExpect(MockMvcResultMatchers.jsonPath("$.previewCards[2].previewContent").value("content6-1"));

    }

}