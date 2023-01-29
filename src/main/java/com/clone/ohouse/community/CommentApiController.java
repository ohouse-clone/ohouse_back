package com.clone.ohouse.community;

import com.clone.ohouse.account.auth.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.community.domain.CommentService;
import com.clone.ohouse.community.domain.comment.Comment;
import com.clone.ohouse.community.domain.comment.dto.CommentListResponseDto;
import com.clone.ohouse.community.domain.comment.dto.CommentResponseDto;
import com.clone.ohouse.community.domain.comment.dto.CommentSaveDto;
import com.clone.ohouse.error.ErrorResponse;
import com.clone.ohouse.error.comment.CommentFailException;
import com.clone.ohouse.error.order.PaymentError;
import io.swagger.annotations.*;
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

    @ApiOperation(
            value = "댓글 저장",
            code = 201,
            response = Long.class,
            notes = "댓글 저장에 사용합니다."
    )
    @ApiResponses({
            @ApiResponse(code = 201, response = Long.class, message = "저장에 성공한 댓글의 id"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "CommentError 참조")
    })
    @PostMapping
    public HttpEntity save(
           @ApiParam(value = "댓글 저장 요청 객체", required = true) @RequestBody CommentSaveDto commentSaveDto
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
            return new ResponseEntity(commentService.save(sessionUser, commentSaveDto), HttpStatus.CREATED);
        } catch (CommentFailException e) {
            log.info(e.getMessage());
            return new ResponseEntity(new ErrorResponse(e.getCommentError().name(), e.getCommentError().getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(
            value = "댓글 삭제",
            code = 200,
            notes = "댓글 삭제에 사용됩니다."
    )
    @ApiResponse(code = 200, message = "삭제 성공시 code 200, 리턴 객체 없음")
    @DeleteMapping("/{id}")
    public HttpEntity delete(
            @ApiParam(value = "comment 의 id", required = true) @PathVariable Long id) throws Exception {
        commentService.delete(id);

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(
            value = "댓글 조회",
            code = 200,
            response = CommentResponseDto.class,
            notes = "댓글을 한개를 조회합니다. <br>" +
                    "성공시 CommentResponseDto.class 객체를, 실패시 ErrorResponse.class 객체를 반환합니다. <br>" +
                    "실패시 ErrorResponse 는 errorCode 반환합니다. 코드에 대한 상세한 정보는 CommentError 를 참조하세요.")
    @ApiResponses({
            @ApiResponse(code = 200, response = CommentResponseDto.class, message = "댓글 한개의 상세 정보"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "CommentError 참조")
    })
    @GetMapping("/{id}")
    public HttpEntity findById(
          @ApiParam(value = "comment 의 id", required = true)  @PathVariable Long id) throws Exception {
        try {
            CommentResponseDto response = commentService.findByCommentId(id);

            return new ResponseEntity(response, HttpStatus.OK);
        } catch (CommentFailException e) {
            log.info(e.getMessage());
            return new ResponseEntity(new ErrorResponse(e.getCommentError().name(), e.getCommentError().getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(
            value = "댓글 다수 조회",
            code = 200,
            response = CommentResponseDto.class,
            notes = "댓글을 여러개를 조회합니다. <br>" +
                    "parameter 로 postId 를 사용하면 post id 별 댓글 다수가 조회됩니다. <br>" +
                    "parameter 를 사용하지 않으면 user id 별 댓글 다수가 조회됩니다. <br>" +
                    "성공시 CommentListResponseDto.class 객체를, 실패시 ErrorResponse.class 객체를 반환합니다. <br>" +
                    "실패시 ErrorResponse 는 errorCode 반환합니다. 코드에 대한 상세한 정보는 CommentError 를 참조하세요.")
    @ApiResponses({
            @ApiResponse(code = 200, response = CommentListResponseDto.class, message = "댓글 한개의 상세 정보"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "CommentError 참조")
    })
    @GetMapping("/bundle")
    public HttpEntity findBundleById(
            @ApiParam(value = "post 의 id", required = false) @RequestParam(required = false) Long postId
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
            if (postId != null) {
                CommentListResponseDto response = commentService.findBundleByPostId(postId);
                return new ResponseEntity(response, HttpStatus.OK);
            } else {
                CommentListResponseDto response = commentService.findBundleByUserId(sessionUser);
                return new ResponseEntity(response, HttpStatus.OK);
            }
        } catch (CommentFailException e) {
            log.info(e.getMessage());
            return new ResponseEntity(new ErrorResponse(e.getCommentError().name(), e.getCommentError().getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @ApiOperation(
            value = "댓글 좋아요 카운트 하나 증가",
            code = 200,
            notes = "댓글의 좋이요 키운트를 하나 증가시킵니다. <br>" +
                    "성공시 객체 반환이 없으며, 실패시 ErrorResponse.class 객체를 반환합니다. <br>" +
                    "실패시 ErrorResponse 는 errorCode 반환합니다. 코드에 대한 상세한 정보는 CommentError 를 참조하세요.")
    @ApiResponses({
            @ApiResponse(code = 200,  message = "성공시 별다른 반환 객체가 없습니다."),
            @ApiResponse(code = 400, response = ErrorResponse.class,  message = "CommentError 참조")
    })
    @PostMapping("/like")
    public HttpEntity ActLike(
           @ApiParam(value = "댓글의 id", required = true) @RequestParam Long commentId
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
        } catch (CommentFailException e) {
            log.info(e.getMessage());
            return new ResponseEntity(new ErrorResponse(e.getCommentError().name(), e.getCommentError().getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

}
