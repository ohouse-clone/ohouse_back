package com.clone.ohouse.store.error.order;

public enum OrderError {
    //주문 상태 변환
    STATE_CANCEL_NOT_CHANGED( "주문 상태 CANCEL 는 변경될 수 없습니다."),
    STATE_READY_NOT_CHANGED("주문 상태 READY 는 변경될 수 없습니다."),
    STATE_CHARGE_NOT_CHANGED("주문 상태 CHARGE 는 인위적으로 변경될 수 없습니다."),

    //주문 취소 실패
    REFUND_HAVE_TO_CHARGE("주문 취소는 결제(CHARGE) 상태에서만 진행할 수 있습니다."),

    //찾으려는 user 없음
    WRONG_USER_ID("로그인한 User 가 적합한 상태가 아닙니다."),
    WRONG_ORDER_ID("찾으려는 orderId 가 없습니다."),
    WRONG_STORE_POST_ID("storePost id 가 없거나 잘못되었습니다.")
    ;

    private String message;

    OrderError(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}

