package com.clone.ohouse.error.order;



public enum OrderError {
    /** 현재 주문 상태에서 거부 **/
    CURRENT_STATE_CANT_CHANGE("READY, CANCEL, REFUND 상태는 변경할 수 없습니다."),

    /** 주문 상태 변환 **/
    STATE_CANCEL_NOT_CHANGED( "주문 상태 CANCEL 는 변경될 수 없습니다."),
    STATE_READY_NOT_CHANGED("주문 상태 READY 는 변경될 수 없습니다."),
    STATE_CHARGED_NOT_CHANGED("주문 상태 CHARGE 는 인위적으로 변경될 수 없습니다."),
    STATE_REFUND_NOT_CHANGED("주문 상태 REFUND 는 인위적으로 변경될 수 없습니다."),
    REFUND_HAVE_TO_CHARGE("주문 취소는 결제(CHARGE) 상태에서만 진행할 수 있습니다."),

    /** User **/
    WRONG_USER_ID("로그인한 User 가 적합한 상태가 아닙니다."),

    /** Order **/
    WRONG_ORDER_ID("찾으려는 orderId 가 없습니다."),

    /** StorePosts **/
    WRONG_STORE_POST_ID("storePost id 가 없거나 잘못되었습니다."),

    /** Payment **/
    FAIL_PAYMENT_CANCEL("결제 취소 실패, 취소 요청은 정상 수행 하지만 파라미터 불일치로 취소 실패"),

    /** Product **/
    //주문하려는 상품 목록이 없음
    NOTHING_PRODUCT_IDS("주문하려는 상품 목록이 없습니다."),
    //주문 하려는 상품(Product) id가 잘못됨
    WRONG_PRODUCT_ID("주문 하려는 상품 id가 존재하지 않는 id입니다."),
    NO_ENOUGH_STOCK("재고보다 많이 주문할 수 없습니다.")
    ;

    private String message;

    OrderError(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

}

