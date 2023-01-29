package com.clone.ohouse.community;

import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.account.domain.user.UserRepository;
import com.clone.ohouse.community.domain.CommentService;
import com.clone.ohouse.community.domain.Post;
import com.clone.ohouse.community.domain.PostType;
import com.clone.ohouse.community.domain.cardcollections.*;
import com.clone.ohouse.community.domain.comment.Comment;
import com.clone.ohouse.community.domain.comment.CommentRepository;
import com.clone.ohouse.community.domain.comment.dto.CommentSaveDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    private final String mappingUrl = "/community/api/v1/comment";
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CardRepository cardRepository;

    private User savedUser;
    private Post savedPost1;
    private Post savedPost2;


    @BeforeEach
    void setup() throws Exception {
        User user = userRepository.save(User.builder()
                .name("TESTER_1")
                .nickname("TSR_1")
                .phone("010-0000-0000")
                .password("1234")
                .email("tester_1@cloneohouse.shop")
                .build());

        savedUser = user;
        savedPost1 = cardRepository.save(new Card(PostType.CARD, HousingType.COMMERCIAL, HouseStyle.CLASSIC, Color.BLACK, savedUser));
        savedPost2 = cardRepository.save(new Card(PostType.CARD, HousingType.APARTMENT, HouseStyle.MODERN, Color.RED, savedUser));
    }

    @AfterEach
    void clean() {
        commentRepository.deleteAll();;
        cardRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void save() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl;
        CommentSaveDto commentSaveDto = new CommentSaveDto(savedPost1.getId(), "첫댓글1");

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(commentSaveDto)))
                .andDo(print());

        //then
        perform.andExpect(MockMvcResultMatchers.status().isCreated());
        Long response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), Long.class);
        Comment comment = commentRepository.findByCommentId(response).orElseThrow(() -> new NoSuchElementException("해당 id 잘못됨 : " + response));
        Assertions.assertThat(comment.getContent()).isEqualTo("첫댓글1");
        Assertions.assertThat(comment.getUser().getNickname()).isEqualTo("TSR_1");
        Assertions.assertThat(comment.getPost().getId()).isEqualTo(savedPost1.getId());
    }

    @Test
    public void delete() throws Exception {
        //given
        Comment save = commentRepository.save(new Comment("코멘트!", savedUser, savedPost1));
        String url = "http://localhost:" + port + mappingUrl;


        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders
                        .delete(url + "/" + save.getId()))
                .andDo(print());

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        Assertions.assertThat(commentRepository.count()).isEqualTo(0);
    }
    @Test
    public void findById() throws Exception {
        //given
        Comment save1 = commentRepository.save(new Comment("코멘트1!", savedUser, savedPost1));
        Comment save2 = commentRepository.save(new Comment("코멘트2!", savedUser, savedPost1));
        String url = "http://localhost:" + port + mappingUrl;

        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders
                        .get(url + "/" + save2.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        perform.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(save2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postId").value(savedPost1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("코멘트2!"));
    }

    @Test
    public void findBundleByPostId() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl + "/bundle";
        User user2 = userRepository.save(User.builder()
                .name("TESTER_2")
                .nickname("TSR_2")
                .phone("010-0000-0000")
                .password("5678")
                .email("tester_2@cloneohouse.shop")
                .build());
        Comment save1 = commentRepository.save(new Comment("코멘트1!", savedUser, savedPost1));
        Comment save2 = commentRepository.save(new Comment("코멘트2!", savedUser, savedPost1));
        Comment save3 = commentRepository.save(new Comment("코멘트3!", savedUser, savedPost1));
        Comment save4 = commentRepository.save(new Comment("코멘트4!", user2, savedPost1));
        Comment save5 = commentRepository.save(new Comment("코멘트5!", user2, savedPost1));
        Comment save6 = commentRepository.save(new Comment("코멘트6!", user2, savedPost2));
        Comment save7 = commentRepository.save(new Comment("코멘트7!", user2, savedPost2));
        Comment save8 = commentRepository.save(new Comment("코멘트8!", user2, savedPost2));


        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .queryParam("postId", String.valueOf(savedPost2.getId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        perform.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalNum").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentList[0].content").value("코멘트6!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentList[1].content").value("코멘트7!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentList[2].content").value("코멘트8!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentList[0].userNickName").value("TSR_2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentList[1].userNickName").value("TSR_2"));
    }

    @Test
    public void findBundleByUserId() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl +"/bundle";
        User user2 = userRepository.save(User.builder()
                .name("TESTER_2")
                .nickname("TSR_2")
                .phone("010-0000-0000")
                .password("5678")
                .email("tester_2@cloneohouse.shop")
                .build());
        Comment save1 = commentRepository.save(new Comment("코멘트1!", savedUser, savedPost1));
        Comment save2 = commentRepository.save(new Comment("코멘트2!", savedUser, savedPost1));
        Comment save3 = commentRepository.save(new Comment("코멘트3!", savedUser, savedPost1));
        Comment save4 = commentRepository.save(new Comment("코멘트4!", user2, savedPost1));
        Comment save5 = commentRepository.save(new Comment("코멘트5!", user2, savedPost1));
        Comment save6 = commentRepository.save(new Comment("코멘트6!", user2, savedPost2));
        Comment save7 = commentRepository.save(new Comment("코멘트7!", user2, savedPost2));
        Comment save8 = commentRepository.save(new Comment("코멘트8!", user2, savedPost2));


        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        perform.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalNum").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentList[0].content").value("코멘트1!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentList[1].content").value("코멘트2!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentList[2].content").value("코멘트3!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentList[0].userNickName").value("TSR_1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentList[1].userNickName").value("TSR_1"));
    }

    @Test
    public void like() throws Exception {
        //given
        String url = "http://localhost:" + port + mappingUrl +"/like";
        Comment save1 = commentRepository.save(new Comment("코멘트1!", savedUser, savedPost1));
        Comment save2 = commentRepository.save(new Comment("코멘트2!", savedUser, savedPost1));


        //when
        ResultActions perform = mvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .queryParam("commentId", String.valueOf(save2.getId())))
                .andDo(print());

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        Comment findComment = commentRepository.findByCommentId(save2.getId()).orElseThrow(() -> new NoSuchElementException("id 잘못됨 : " + save2.getId()));
        Assertions.assertThat(findComment.getLikeNumber()).isEqualTo(1);
    }
}
