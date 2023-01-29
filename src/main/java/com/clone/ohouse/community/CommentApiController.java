package com.clone.ohouse.community;

import com.clone.ohouse.account.auth.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.community.domain.CommentService;
import com.clone.ohouse.community.domain.comment.dto.CommentListResponseDto;
import com.clone.ohouse.community.domain.comment.dto.CommentResponseDto;
import com.clone.ohouse.community.domain.comment.dto.CommentSaveDto;
import com.clone.ohouse.error.ErrorResponse;
import com.clone.ohouse.error.comment.CommentFailException;
import com.clone.ohouse.error.order.PaymentError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/community/api/v1/comment")
@RestController
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping
    public HttpEntity save(
            @RequestBody CommentSaveDto commentSaveDto) throws Exception{
        //TODO: Temporary users, 추후 삭제 예정
        SessionUser sessionUser = null;
        if (sessionUser == null) {
            sessionUser = new SessionUser(User.builder()
                    .name("TESTER_1")
                    .nickname("TSR_1")
                    .phone("010-0000-0000")
                    .password("1234")
                    .email("tester_1@cloneohouse.shop")
                    .build());
        }

        try {
            return new ResponseEntity(commentService.save(sessionUser, commentSaveDto), HttpStatus.CREATED);
        }
        catch(CommentFailException e){
            log.info(e.getMessage());
            return new ResponseEntity(new ErrorResponse(e.getCommentError().name(), e.getCommentError().getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public HttpEntity delete(
            @PathVariable Long id) throws Exception{
        commentService.delete(id);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public HttpEntity findById(
            @PathVariable Long commentId) throws Exception {
        try {
            CommentResponseDto response = commentService.findByCommentId(commentId);

            return new ResponseEntity(response, HttpStatus.OK);
        } catch(CommentFailException e){
            log.info(e.getMessage());
            return new ResponseEntity(new ErrorResponse(e.getCommentError().name(), e.getCommentError().getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/bundle")
    public HttpEntity findBundleById(
            @RequestParam(required = false) Long postId
    ) throws Exception{

        //TODO: Temporary users, 추후 삭제 예정
        SessionUser sessionUser = null;
        if (sessionUser == null) {
            sessionUser = new SessionUser(User.builder()
                    .name("TESTER_1")
                    .nickname("TSR_1")
                    .phone("010-0000-0000")
                    .password("1234")
                    .email("tester_1@cloneohouse.shop")
                    .build());
        }
        try {
            if(postId != null) {
                CommentListResponseDto response = commentService.findBundleByPostId(postId);
                return new ResponseEntity(response, HttpStatus.OK);
            }
            else {
                CommentListResponseDto response = commentService.findBundleByUserId(sessionUser);
                return new ResponseEntity(response, HttpStatus.OK);
            }
        } catch (CommentFailException e){
            log.info(e.getMessage());
            return new ResponseEntity(new ErrorResponse(e.getCommentError().name(), e.getCommentError().getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/like")
    public HttpEntity ActLike(
            @RequestParam Long commentId
    ) throws Exception {
        //TODO: Temporary users, 추후 삭제 예정
        SessionUser sessionUser = null;
        if (sessionUser == null) {
            sessionUser = new SessionUser(User.builder()
                    .name("TESTER_1")
                    .nickname("TSR_1")
                    .phone("010-0000-0000")
                    .password("1234")
                    .email("tester_1@cloneohouse.shop")
                    .build());
        }

        try {
            commentService.actLike(commentId);

            return new ResponseEntity(HttpStatus.OK);
        }
        catch(CommentFailException e){
            log.info(e.getMessage());
            return new ResponseEntity(new ErrorResponse(e.getCommentError().name(), e.getCommentError().getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

}
