package com.clone.ohouse.store;

import com.clone.ohouse.account.auth.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.store.domain.OrderService;
import com.clone.ohouse.store.domain.PaymentService;
import com.clone.ohouse.store.domain.order.dto.*;
import com.clone.ohouse.store.domain.payment.dto.PaymentUserCancelResponse;
import com.clone.ohouse.store.domain.payment.dto.PaymentUserFailResponseDto;
import com.clone.ohouse.store.domain.payment.dto.PaymentUserSuccessResponseDto;
import com.clone.ohouse.error.ErrorResponse;
import com.clone.ohouse.error.order.OrderError;
import com.clone.ohouse.error.order.OrderFailException;
import com.clone.ohouse.error.order.PaymentFailException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/order/api/v1")
@RestController
public class OrderApiController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    @ApiOperation(
            value = "주문 시작",
            code = 200,
            response = OrderResponse.class,
            notes = "주문 시작에 사용됩니다. <br>" +
                    "주문은 2단계로 나뉘어져 있으며 첫번째 단계에서는 이 메서드를 호출하세요.<br>" +
                    "두 번째 단계에서는 toss payment api를 호출합니다.<br>" +
                    "이 메서드는 주문을 생성하고 orderId가 포함된 객체를 결과로 반환합니다. <br>" +
                    "orderId는 toss payment api에서 사용됩니다. <br>" +
                    "주문은 생성시 READY 상태를 가지며 (첫번째 단계) 결제 이후 CHARGED 상태 (두번째 단계)로 변경됩니다.<br>" +
                    "성공시 OrderResponse.class 객체를, 실패시 ErrorResponse.class 객체를 반환합니다. <br>" +
                    "실패시 ErrorResponse 는 errorCode 반환합니다. 코드에 대한 상세한 정보는 OrderError 를 참조하세요.")
    @ApiResponses({
                    @ApiResponse(code = 200, response = OrderResponse.class, message = "첫번째 주문 절차 성공"),
                    @ApiResponse(code = 400, response = ErrorResponse.class, message = "OrderError 참조")})
    @PostMapping("/order")
    public HttpEntity startOrder(
             @ApiParam(value = "주문 시작에 필요한 객체", required = true) @RequestBody StartOrderRequestDto startOrderRequestDto
//            @LoginUser SessionUser sessionUser,
    ) throws Exception {

        //orderList가 NULL 일 경우
        List<OrderedProductDto> orderList = startOrderRequestDto.getOrderRequestDto().getOrderList();
        if (orderList == null || orderList.size() == 0)
            return new ResponseEntity(new ErrorResponse(OrderError.NOTHING_PRODUCT_IDS.name(), OrderError.NOTHING_PRODUCT_IDS.getMessage()), HttpStatus.BAD_REQUEST);

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
            OrderResponse orderResponse = orderService.startOrder(sessionUser, startOrderRequestDto.getOrderRequestDto(), startOrderRequestDto.getDeliveryDto());
            return new ResponseEntity(orderResponse, HttpStatus.OK);
        } catch (OrderFailException e) {
            log.info(e.getMessage());
            return new ResponseEntity(new ErrorResponse(e.getOrderError().name(), e.getOrderError().getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(
            value = "주문 상세 조회",
            code = 200,
            response = OrderDetailResponseDto.class,
            notes = "주문 한 개를 상세히 조회합니다. <br>" +
                    "성공시 OrderDetailResponseDto.class 객체를, 실패시 ErrorResponse.class 객체를 반환합니다. <br>" +
                    "실패시 ErrorResponse 는 errorCode 반환합니다. 코드에 대한 상세한 정보는 OrderError 를 참조하세요.")
    @ApiResponses({
            @ApiResponse(code = 200, response = OrderDetailResponseDto.class, message = "주문 조회 성공"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "OrderError 참조")})
    @GetMapping("/order/{orderId}")
    public HttpEntity findOrderDetail(
            @ApiParam(value = "주문 생성시 얻는 orderId", required = true) @PathVariable String orderId) throws Exception {
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
            OrderDetailResponseDto orderDetail = orderService.findOrderDetail(sessionUser, orderId);
            return new ResponseEntity(orderDetail, HttpStatus.OK);
        } catch (OrderFailException e) {
            log.info(e.getMessage());
            return new ResponseEntity(new ErrorResponse(e.getOrderError().name(), e.getOrderError().getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * User가 결제한 주문 리스트 반환
     *
     * @return OrderBundleView
     */
    @ApiOperation(
            value = "주문 리스트 조회",
            code = 200,
            response = OrderBundleViewDto.class,
            notes = "주문 리스트를 조회합니다. <br>" +
                    "성공시 OrderBundleViewDto.class 객체를, 실패시 ErrorResponse.class 객체를 반환합니다. <br>" +
                    "실패시 ErrorResponse 는 errorCode 반환합니다. 코드에 대한 상세한 정보는 OrderError 를 참조하세요.")
    @ApiResponses({
            @ApiResponse(code = 200, response = OrderBundleViewDto.class, message = "주문 리스트 조회 성공"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "OrderError 참조")})
    @GetMapping("/orders")
    public HttpEntity findAllOrders() throws Exception {

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
            OrderBundleViewDto orderBundleViewDto = orderService.findAllOrders(sessionUser);
            return new ResponseEntity(orderBundleViewDto, HttpStatus.OK);
        } catch (OrderFailException e) {
            log.info(e.getMessage());
            return new ResponseEntity(new ErrorResponse(e.getOrderError().name(), e.getOrderError().getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(
            value = "주문 상태 변경",
            code = 200,
            notes = "주문의 상태를 변경합니다. <br>" +
                    "변경하려는 주문 상태가 READY, CANCEL, CHARGED, REFUND 는 이 메서드로 변경할 수 없습니다.<br>" +
                    "또한 현재 발행되있는 주문의 상태가 READY, CANCEL, REFUND 상태는 변경될 수 없습니다.<br>" +
                    "성공시 HTTP Response code 200을 줍니다. 별도의 반환 객체는 없습니다. <br>" +
                    "실패시 ErrorResponse 는 errorCode 반환합니다. 코드에 대한 상세한 정보는 OrderError 를 참조하세요.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "주문 상태 변경 성공"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "OrderError 참조")})
    @PostMapping("/order/{orderId}/status")
    public HttpEntity SetOrderStatus(
            @ApiParam(value = "주문 생성시 얻는 orderId", required = true) @PathVariable String orderId,
            @ApiParam(value = "OrderStatus 가 포함된 객체", required = true) @RequestBody OrderSetStatusRequestDto orderSetStatusRequestDto) throws Exception {

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
            orderService.changeOrderState(sessionUser, orderId, orderSetStatusRequestDto.getStatus());

            return new ResponseEntity(HttpStatus.OK);
        } catch (OrderFailException e) {
            log.info(e.getMessage());

            return new ResponseEntity(new ErrorResponse(e.getOrderError().name(), e.getOrderError().getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(
            value = "주문 취소",
            response = PaymentUserCancelResponse.class,
            code = 200,
            notes = "주문을 취소합니다 <br>" +
                    "주문 취소는 주문의 상태가 CHARGED 일때만 변경할 수 있습니다.<br>" +
                    "따라서 상태를 CHARGED -> REFUND 로 변경합니다. <br>" +
                    "해당 메서드는 카드 결제 취소를 포함합니다.<br>" +
                    "성공시 paymentUserCancelResponse.class 객체를 줍니다. <br>" +
                    "실패시 ErrorResponse 는 errorCode 반환합니다. 코드에 대한 상세한 정보는 OrderError 또는 PaymentError 를 참조하세요.")
    @ApiResponses({
            @ApiResponse(code = 200, response = PaymentUserCancelResponse.class, message = "주문 취소 성공"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "OrderError 참조 또는 PaymentError 참조")})
    @PostMapping("/payment/{orderId}/card/cancel")
    public HttpEntity cancelPayment(
            @ApiParam(value = "주문 생성시 얻는 orderId", required = true) @PathVariable("orderId") String orderId,
            @ApiParam(value = "취소 사유가 포함된 객체", required = true) @RequestBody OrderCancelPaymentRequestDto orderCancelPaymentRequestDto) throws Exception {

        try {
            PaymentUserCancelResponse paymentUserCancelResponse = paymentService.requestCancel(orderId, orderCancelPaymentRequestDto.getCancelReason());

            return new ResponseEntity(paymentUserCancelResponse, HttpStatus.OK);
        } catch (OrderFailException e) {
            log.info(e.getMessage());
            return new ResponseEntity(
                    new ErrorResponse(e.getOrderError().name(), e.getOrderError().getMessage()),
                    HttpStatus.BAD_REQUEST);
        } catch (PaymentFailException e) {
            log.info("toss request fail");
            log.info(e.getMessage());
//            log.info(e.getTossFailObject().toString());
            return new ResponseEntity(
                    e.getTossFailObject(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @ApiOperation(
            value = "",
            hidden = true
    )
    @GetMapping("/payment/card/success")
    public HttpEntity<PaymentUserSuccessResponseDto> requestOrderCompleteSuccess(
            @RequestParam("paymentKey") String paymentKey,
            @RequestParam("orderId") String orderApprovalCode,
            @RequestParam("amount") Long amount
    ) {
        try {
            paymentService.verifyPaymentComplete(paymentKey, orderApprovalCode, amount);

            PaymentUserSuccessResponseDto response = paymentService.requestPaymentComplete(paymentKey, orderApprovalCode, amount);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(
            value = "",
            hidden = true
    )
    @GetMapping("/payment/card/fail")
    public HttpEntity<PaymentUserFailResponseDto> requestOrderCompleteFail(
            @RequestParam("code") String code,
            @RequestParam("message") String message,
            @RequestParam("orderId") String orderId
    ) {
        try {
            orderService.cancel(orderId);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new PaymentUserFailResponseDto(
                code,
                message,
                orderId
        ), HttpStatus.OK);
    }

}
