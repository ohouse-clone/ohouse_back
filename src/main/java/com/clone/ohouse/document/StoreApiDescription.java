package com.clone.ohouse.document;

public class StoreApiDescription {
    public static final String description =
            "<h3>OrderError Code</h3>" +
                    "Order API 에서 Request Fail 시 발생할 수 있는 Code 입니다." + "<br>" +
                    "<dl>" +
                    "<dt>CURRENT_STATE_CANT_CHANGE</dt>" +
                    "<dd> - READY, CANCEL, REFUND 상태는 변경할 수 없습니다. </dd>" +

                    "<dt>STATE_CANCEL_NOT_CHANGED</dt>" +
                    "<dd> - 주문 상태 CANCEL 는 변경될 수 없습니다.</dd>" +

                    "<dt>STATE_READY_NOT_CHANGED</dt>" +
                    "<dd> - 주문 상태 READY 는 변경될 수 없습니다.</dd>" +

                    "<dt>STATE_CHARGED_NOT_CHANGED</dt>" +
                    "<dd> - 주문 상태 CHARGE 는 인위적으로 변경될 수 없습니다.</dd>" +

                    "<dt>STATE_REFUND_NOT_CHANGED</dt>" +
                    "<dd> - 주문 상태 REFUND 는 인위적으로 변경될 수 없습니다.</dd>" +

                    "<dt>REFUND_HAVE_TO_CHARGE</dt>" +
                    "<dd> - 주문 취소는 결제(CHARGE) 상태에서만 진행할 수 있습니다.</dd>" +

                    "<dt>WRONG_USER_ID</dt>" +
                    "<dd> - 로그인한 User 가 적합한 상태가 아닙니다.</dd>" +

                    "<dt>WRONG_ORDER_ID</dt>" +
                    "<dd> - 찾으려는 orderId 가 없습니다.</dd>" +

                    "<dt>WRONG_STORE_POST_ID</dt>" +
                    "<dd> - storePost id 가 없거나 잘못되었습니다.</dd>" +

                    "<dt>FAIL_PAYMENT_CANCEL</dt>" +
                    "<dd> - 결제 취소 실패, 취소 요청은 정상 수행 하지만 파라미터 불일치로 취소 실패</dd>" +

                    "<dt>NOTHING_PRODUCT_IDS</dt>" +
                    "<dd> - 주문하려는 상품 목록이 없습니다.</dd>" +

                    "<dt>WRONG_PRODUCT_ID</dt>" +
                    "<dd> - 주문 하려는 상품 id가 존재하지 않는 id 입니다.</dd>" +

                    "<dt>NO_ENOUGH_STOCK</dt>" +
                    "<dd> - 재고보다 많이 주문할 수 없습니다.</dd>" +
                    "</dl>" +


                    "<h3>PaymentError Code</h3>" +
                    "Order API 에서 Request Fail 시 발생할 수 있는 Code 입니다." + "<br>" +
                    "<dl>" +

                    "<dt>FAIL_CANCEL_REQUEST_TO_TOSS</dt>" +
                    "<dd> - toss 로의 cancel request 실패</dd>" +
                    "</dl>";
}
