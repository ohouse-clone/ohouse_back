package com.clone.ohouse.error.comment;

import lombok.Getter;

@Getter
public class CommentFailException extends RuntimeException {
    private final CommentError commentError;
    public CommentFailException(String message, CommentError commentError) {
        super("ErrorCode=" + commentError.name() + ", " +message);
        this.commentError = commentError;
    }


    public CommentError getError() {
        return commentError;
    }
}
