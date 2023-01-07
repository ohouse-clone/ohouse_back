package com.clone.ohouse.store.error.order;

public enum PaymentError {

    FAIL_CANCEL_REQUEST_TO_TOSS("toss 로의 cancel request 실패")
    ;
    private String message;

    PaymentError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
