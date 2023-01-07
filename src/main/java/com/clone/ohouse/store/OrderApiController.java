package com.clone.ohouse.store;

import com.amazonaws.Response;
import com.clone.ohouse.account.auth.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.store.domain.OrderService;
import com.clone.ohouse.store.domain.PaymentService;
import com.clone.ohouse.store.domain.order.dto.OrderBundleViewDto;
import com.clone.ohouse.store.domain.order.dto.OrderDetailResponseDto;
import com.clone.ohouse.store.domain.order.dto.OrderResponse;
import com.clone.ohouse.store.domain.order.dto.StartOrderRequestDto;
import com.clone.ohouse.store.domain.payment.dto.PaymentUserCancelResponse;
import com.clone.ohouse.store.domain.payment.dto.PaymentUserFailResponseDto;
import com.clone.ohouse.store.domain.payment.dto.PaymentUserSuccessResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/order/api/v1")
@RestController
public class OrderApiController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    @PostMapping("/order")
    public OrderResponse startOrder(
            @RequestBody StartOrderRequestDto startOrderRequestDto
//            @LoginUser SessionUser sessionUser
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
        //TODO: 주문할 프로덕트 리스트 NULL이면 Fail
        return orderService.startOrder(sessionUser, startOrderRequestDto.getOrderRequestDto(), startOrderRequestDto.getDeliveryDto());
    }

    @GetMapping("/order")
    public OrderDetailResponseDto findOrderDetail(
            @RequestParam String orderId) throws Exception{
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

        OrderDetailResponseDto orderDetail = orderService.findOrderDetail(sessionUser, orderId);

        return orderDetail;

    }


    /**
     * User가 결제한 주문 리스트 반환
     *
     * @return OrderBundleView
     */
    @GetMapping("/orders")
    public OrderBundleViewDto findAllOrders() throws Exception {

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

        OrderBundleViewDto orderBundleViewDto = orderService.findAllOrders(sessionUser);

        return orderBundleViewDto;
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

    @PostMapping("/payment/card/cancel")
    public HttpEntity<PaymentUserCancelResponse> cancelPayment(
            @RequestParam("orderId") String orderId,
            @RequestParam("cancelReason") String cancelReason) {

        try {
            log.info("Cancel Payment");
            log.info("orderId : " + orderId);
            log.info("cancelReason : " + cancelReason);
            PaymentUserCancelResponse paymentUserCancelResponse = paymentService.requestCancel(orderId, cancelReason);

            return new ResponseEntity<>(paymentUserCancelResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
