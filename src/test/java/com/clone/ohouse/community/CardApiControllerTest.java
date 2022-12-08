package com.clone.ohouse.community;

import com.clone.ohouse.account.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.account.domain.user.UserRepository;
import com.clone.ohouse.community.domain.CardService;
import com.clone.ohouse.community.domain.cardcollections.*;
import com.clone.ohouse.community.domain.cardcollections.dto.CardSaveRequestContentDto;
import com.clone.ohouse.community.domain.cardcollections.dto.CardSaveRequestHeaderDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
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
                        .birthday("birth")
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
        MockMultipartFile sample = new MockMultipartFile("files", "file.png", "image/png", "file".getBytes(StandardCharsets.UTF_8));

//        String content = objectMapper.writeValueAsString(new MetaData("name", "stringSomething", 100));
//        MockMultipartFile json = new MockMultipartFile("meta-data", "jsondata", "application/json", content.getBytes(StandardCharsets.UTF_8));

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.multipart(url)
//                        .part(new MockPart("header", new ObjectMapper().writeValueAsString(headerDto).getBytes(StandardCharsets.UTF_8)))
                        .file(new MockMultipartFile("header", "json", "application/json", objectMapper.writeValueAsString(headerDto).getBytes(StandardCharsets.UTF_8)))
                        .file(multipartFiles[0])
//                                .file(sample)
                        .file(multipartFiles[1])
                        .file(multipartFiles[2])
                        //.part(new MockPart("contents", new ObjectMapper().writeValueAsString(contentDtos[0]).getBytes(StandardCharsets.UTF_8)))
                        //.part(new MockPart("contents", new ObjectMapper().writeValueAsString(contentDtos[1]).getBytes(StandardCharsets.UTF_8)))
                        //.part(new MockPart("contents", new ObjectMapper().writeValueAsString(contentDtos[2]).getBytes(StandardCharsets.UTF_8))))
                        .file(new MockMultipartFile("contents", "json", "application/json", objectMapper.writeValueAsString(contentDtos[0]).getBytes(StandardCharsets.UTF_8)))
                        .file(new MockMultipartFile("contents", "json", "application/json", objectMapper.writeValueAsString(contentDtos[1]).getBytes(StandardCharsets.UTF_8)))
                        .file(new MockMultipartFile("contents", "json", "application/json", objectMapper.writeValueAsString(contentDtos[2]).getBytes(StandardCharsets.UTF_8)))
        ).andDo(print());

        //then
        perform.andExpect(MockMvcResultMatchers.status().isCreated());
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

}