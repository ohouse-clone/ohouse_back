package com.clone.ohouse.store;

import com.clone.ohouse.account.auth.SessionUser;
import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.store.domain.OrderService;
import com.clone.ohouse.store.domain.PaymentService;
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

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/order/api/v1/payment")
@RestController
public class OrderApiController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    @PostMapping
    public OrderResponse startOrder(
            @RequestBody StartOrderRequestDto startOrderRequestDto
//            @LoginUser SessionUser sessionUser
            ) throws Exception{

        //TODO: Temporary users, 추후 삭제 예정
        SessionUser sessionUser = null;
        if(sessionUser == null){
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

    @GetMapping("/card/success")
    public HttpEntity<PaymentUserSuccessResponseDto> requestOrderCompleteSuccess(
            @RequestParam("paymentKey") String paymentKey,
            @RequestParam("orderId") String orderApprovalCode,
            @RequestParam("amount") Long amount
    ){
        try {
            paymentService.verifyPaymentComplete(paymentKey,orderApprovalCode,amount);

            PaymentUserSuccessResponseDto response = paymentService.requestPaymentComplete(paymentKey, orderApprovalCode, amount);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            log.info(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/card/fail")
    public HttpEntity<PaymentUserFailResponseDto> requestOrderCompleteFail(
            @RequestParam("code") String code,
            @RequestParam("message") String message,
            @RequestParam("orderId") String orderId
    ){
        try {
            orderService.cancel(orderId);
        }
        catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new PaymentUserFailResponseDto(
                code,
                message,
                orderId
        ), HttpStatus.OK);
    }

    @PostMapping("/card/cancel")
    public HttpEntity<PaymentUserCancelResponse> cancelPayment(
            @RequestParam("orderId") String orderId,
            @RequestParam("cancelReason") String cancelReason){

            try {
                log.info("Cancel Payment");
                log.info("orderId : " + orderId);
                log.info("cancelReason : " + cancelReason);
                PaymentUserCancelResponse paymentUserCancelResponse = paymentService.requestCancel(orderId, cancelReason);

                return new ResponseEntity<>(paymentUserCancelResponse, HttpStatus.OK);
            }
            catch(Exception e){
                log.info(e.getMessage());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
    }



    //TODO: 결제 조회 구현
    //TODO: 결제한 리스트 구현

}
