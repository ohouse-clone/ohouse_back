package com.clone.ohouse.error.comment;



public enum CommentError {
    /** Comment **/
    WRONG_COMMENT_ID("찾으려는 Comment ID가 없습니다."),

    /** User **/
    WRONG_USER_ID("로그인한 User 가 적합한 상태가 아닙니다."),

    /** Post **/
    WRONG_POST_ID("찾으려는 post ID가 없습니다."),
    ;

    private String message;

    CommentError(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

}

