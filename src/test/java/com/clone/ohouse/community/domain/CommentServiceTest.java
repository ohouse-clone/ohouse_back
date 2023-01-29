package com.clone.ohouse.community.domain;


import com.clone.ohouse.account.auth.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.account.domain.user.UserRepository;
import com.clone.ohouse.community.domain.cardcollections.*;
import com.clone.ohouse.community.domain.comment.Comment;
import com.clone.ohouse.community.domain.comment.CommentRepository;
import com.clone.ohouse.community.domain.comment.dto.CommentListResponseDto;
import com.clone.ohouse.community.domain.comment.dto.CommentResponseDto;
import com.clone.ohouse.community.domain.comment.dto.CommentSaveDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;

    private User saveUser = null;
    private Post savedPost = null;

    @BeforeEach
    void setup(){
        User user = userRepository.save(User.builder()
                .name("TESTER_1")
                .nickname("TSR_1")
                .phone("010-0000-0000")
                .password("1234")
                .email("tester_1@cloneohouse.shop")
                .build());

        saveUser = user;
        savedPost = cardRepository.save(new Card(PostType.CARD, HousingType.COMMERCIAL, HouseStyle.CLASSIC, Color.BLACK, saveUser));
    }

    @AfterEach
    void clean(){
        commentRepository.deleteAll();;
        cardRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void save() throws Exception {
        //given
        SessionUser sessionUser = new SessionUser(User.builder()
                .name("TESTER_1")
                .nickname("TSR_1")
                .phone("010-0000-0000")
                .password("1234")
                .email("tester_1@cloneohouse.shop")
                .build());
        CommentSaveDto commentSaveDto = new CommentSaveDto(savedPost.getId(), "코멘트!");

        //when
        Long savedId = commentService.save(sessionUser, commentSaveDto);

        //then
        Comment comment = commentRepository.findById(savedId).get();
        Assertions.assertThat(comment.getContent()).isEqualTo("코멘트!");
        Assertions.assertThat(comment.getUser().getNickname()).isEqualTo("TSR_1");
    }
    @Test
    void delete() throws Exception {
        //given
        Comment save = commentRepository.save(new Comment("코멘트!", saveUser, savedPost));

        //when
        commentService.delete(save.getId());

        //then
        long count = commentRepository.count();
        Assertions.assertThat(count).isEqualTo(0l);
    }

    @Test
    void findByCommentId() throws Exception{
        //given
        Comment save = commentRepository.save(new Comment("코멘트!", saveUser, savedPost));

        //when
        CommentResponseDto response = commentService.findByCommentId(save.getId());

        //then
        Assertions.assertThat(response.getUserNickName()).isEqualTo("TSR_1");
        Assertions.assertThat(response.getContent()).isEqualTo("코멘트!");
    }

    @Test
    void findBundleByPostId() throws Exception{
        //given
        Comment save1 = commentRepository.save(new Comment("코멘트1!", saveUser, savedPost));
        Comment save2 = commentRepository.save(new Comment("코멘트2!", saveUser, savedPost));
        Comment save3 = commentRepository.save(new Comment("코멘트3!", saveUser, savedPost));

        Post savedPost2 = cardRepository.save(new Card(PostType.CARD, HousingType.SINGLE_HOUSE, HouseStyle.ROMANTIC, Color.BLUE, saveUser));
        Comment save4 = commentRepository.save(new Comment("코멘트4!", saveUser, savedPost2));
        Comment save5 = commentRepository.save(new Comment("코멘트5!", saveUser, savedPost2));
        Comment save6 = commentRepository.save(new Comment("코멘트6!", saveUser, savedPost2));

        //when
        CommentListResponseDto responseList = commentService.findBundleByPostId(savedPost2.getId());

        //then
        Assertions.assertThat(responseList.getTotalNum()).isEqualTo(3);
        Assertions.assertThat(responseList.getCommentList().get(0).getContent()).isEqualTo("코멘트4!");
        Assertions.assertThat(responseList.getCommentList().get(2).getContent()).isEqualTo("코멘트6!");
    }

    @Test
    void findBundleByUserId() throws Exception{
        //given
        User savedUser2 = userRepository.save(User.builder()
                .email("cc@@dd")
                .password("5678")
                .nickname("kkh")
                .phone("010-0000-0000")
                .name("vvp name")
                .build());
        Comment save1 = commentRepository.save(new Comment("코멘트1!", saveUser, savedPost));
        Comment save2 = commentRepository.save(new Comment("코멘트2!", saveUser, savedPost));
        Comment save3 = commentRepository.save(new Comment("코멘트3!", saveUser, savedPost));

        Comment save4 = commentRepository.save(new Comment("코멘트4!", savedUser2, savedPost));
        Comment save5 = commentRepository.save(new Comment("코멘트5!", savedUser2, savedPost));
        Comment save6 = commentRepository.save(new Comment("코멘트6!", savedUser2, savedPost));

        SessionUser sessionUser = new SessionUser(User.builder()
                .email("cc@@dd")
                .password("5678")
                .nickname("kkh")
                .phone("010-0000-0000")
                .name("vvp name")
                .build());

        //when
        CommentListResponseDto responseList = commentService.findBundleByUserId(sessionUser);

        //then
        Assertions.assertThat(responseList.getTotalNum()).isEqualTo(3);
        Assertions.assertThat(responseList.getCommentList().get(0).getContent()).isEqualTo("코멘트4!");
        Assertions.assertThat(responseList.getCommentList().get(2).getContent()).isEqualTo("코멘트6!");
    }

    @Test
    void actLike() throws Exception {
        //given
        Comment save = commentRepository.save(new Comment("코멘트!", saveUser, savedPost));

        //when
        commentService.actLike(save.getId());
        commentService.actLike(save.getId());

        //then
        Comment comment = commentRepository.findById(save.getId()).get();
        Assertions.assertThat(comment.getLikeNumber()).isEqualTo(2);
    }

}
