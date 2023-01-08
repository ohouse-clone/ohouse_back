package com.clone.ohouse.store;

import com.amazonaws.Response;
import com.clone.ohouse.account.auth.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.store.domain.OrderService;
import com.clone.ohouse.store.domain.PaymentService;
import com.clone.ohouse.store.domain.order.OrderStatus;
import com.clone.ohouse.store.domain.order.dto.*;
import com.clone.ohouse.store.domain.payment.dto.PaymentUserCancelResponse;
import com.clone.ohouse.store.domain.payment.dto.PaymentUserFailResponseDto;
import com.clone.ohouse.store.domain.payment.dto.PaymentUserSuccessResponseDto;
import com.clone.ohouse.store.error.ErrorResponse;
import com.clone.ohouse.store.error.order.OrderError;
import com.clone.ohouse.store.error.order.OrderFailException;
import com.clone.ohouse.store.error.order.PaymentFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/order/api/v1")
@RestController
public class OrderApiController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    //TODO: API 문서 내용 추가
    @PostMapping("/order")
    public HttpEntity startOrder(
            @RequestBody StartOrderRequestDto startOrderRequestDto
//            @LoginUser SessionUser sessionUser
    ) throws Exception {

        //orderList가 NULL일 경우
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

    @GetMapping("/order/{orderId}")
    public HttpEntity findOrderDetail(
            @PathVariable String orderId) throws Exception {
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

    @PostMapping("/order/{orderId}/status")
    public HttpEntity SetOrderStatus(
            @PathVariable String orderId,
            @RequestBody OrderSetStatusRequestDto orderSetStatusRequestDto) throws Exception {

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

    @PostMapping("/payment/{orderId}/card/cancel")
    public HttpEntity cancelPayment(
            @PathVariable("orderId") String orderId,
            @RequestBody OrderCancelPaymentRequestDto orderCancelPaymentRequestDto) throws Exception {

        try {
            PaymentUserCancelResponse paymentUserCancelResponse = paymentService.requestCancel(orderId, orderCancelPaymentRequestDto.getCancelReason());

            return new ResponseEntity(paymentUserCancelResponse, HttpStatus.OK);
        } catch (OrderFailException e) {
            log.info(e.getMessage());
            return new ResponseEntity(
                    new ErrorResponse(e.getOrderError().name(), e.getOrderError().getMessage()),
                    HttpStatus.BAD_REQUEST);
        } catch (PaymentFailException e){
            log.info("toss request fail");
            log.info(e.getMessage());
//            log.info(e.getTossFailObject().toString());
            return new ResponseEntity(
                    e.getTossFailObject(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
