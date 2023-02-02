package com.clone.ohouse.store.domain;

import com.clone.ohouse.store.domain.order.Order;
import com.clone.ohouse.store.domain.order.OrderRepository;
import com.clone.ohouse.store.domain.payment.Payment;
import com.clone.ohouse.store.domain.payment.PaymentRepository;
import com.clone.ohouse.store.domain.payment.PaymentResultStatus;
import com.clone.ohouse.store.domain.payment.dto.*;
import com.clone.ohouse.error.order.OrderError;
import com.clone.ohouse.error.order.OrderFailException;
import com.clone.ohouse.error.order.PaymentError;
import com.clone.ohouse.error.order.PaymentFailException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.NoSuchElementException;

@Getter
@RequiredArgsConstructor
@Transactional
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Value("${payments.toss.secret_api_key}")
    private String tossSecretApiKeyForTest;
    @Value("${payments.toss.client_api_key}")
    private String tossClientApiKeyForTest;

    @Value("${payments.toss.card.success_url}")
    private String tossSuccessCallBackUrlForCard;
    @Value("${payments.toss.card.fail_url}")
    private String tossFailCallBackUrlForCard;

    @Value("${payments.toss.card.confirm_url}")
    private String tossRequestPaymentConfirmUrl;

    @Value("${payments.toss.api_url}")
    private String tossApiUrl;

    public Long save(Payment payment){
        return paymentRepository.save(payment).getId();
    }

    public void delete(Long paymentId){
        paymentRepository.deleteById(paymentId);
    }

    public void verifyPaymentComplete(String paymentKey, String orderId, Long amount) throws Exception{
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new RuntimeException("Can't find payment from orderApprovalCode : " + orderId));
        Order order = orderRepository.findByOrderIdWithOrderedProduct(orderId).orElseThrow(() -> new RuntimeException("Can't find order from payment id : " + payment.getId()));

        if(order.getTotalPrice() != amount) throw new RuntimeException("Wrong amount : " + amount);
    }

    public PaymentUserSuccessResponseDto requestPaymentComplete(String paymentKey, String orderId, Long amount){
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String auth = new String(Base64.getEncoder().encode((tossSecretApiKeyForTest + ":").getBytes(StandardCharsets.UTF_8)));

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(auth);

        //Send Request to toss
        ResponseEntity<PaymentCompleteResponseDto> response = template.postForEntity(
                tossRequestPaymentConfirmUrl,
                new HttpEntity<PaymentCompleteRequestDto>(
                        new PaymentCompleteRequestDto(paymentKey, orderId, amount),
                        headers)
                ,
                PaymentCompleteResponseDto.class
        );
        PaymentCompleteResponseDto paymentResponse = response.getBody();

        if(paymentResponse.getStatus().equals(PaymentResultStatus.DONE.toString())){
            //update order status
            Order order = orderRepository.findByOrderIdWithOrderedProduct(orderId).orElseThrow(() -> new NoSuchElementException("잘못된 orderId : " + orderId));
            order.successPayment();

            //Save payment key
            Payment payment = paymentRepository.findByOrderId(orderId).get();
            payment.processPayment(
                    paymentKey,
                    PaymentResultStatus.valueOf(paymentResponse.getStatus()),
                    paymentResponse.getRequestedAt(),
                    paymentResponse.getApprovedAt(),
                    paymentResponse.getTotalAmount(),
                    paymentResponse.getBalanceAmount());
        }


        return new PaymentUserSuccessResponseDto(
                paymentResponse.getRequestedAt(),
                paymentResponse.getApprovedAt(),
                PaymentResultStatus.valueOf(paymentResponse.getStatus()),
                paymentResponse.getTotalAmount(),
                paymentResponse.getBalanceAmount());
    }

    public PaymentUserCancelResponse requestCancel(String orderId, String cancelReason) throws Exception{
        Order order = orderRepository.findByOrderIdWithOrderedProduct(orderId).orElseThrow(() -> new OrderFailException("찾으려는 order 없음, orderId : " + orderId, OrderError.WRONG_ORDER_ID));
        String auth = new String(Base64.getEncoder().encode((tossSecretApiKeyForTest+":").getBytes(StandardCharsets.UTF_8)));
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(auth);

        //Send Request to toss
        ResponseEntity<PaymentCompleteResponseDto> response = template.postForEntity(
                tossApiUrl + "/" + order.getPayment().getPaymentKey() + "/cancel",
                new HttpEntity<PaymentCancelRequestDto>(
                        new PaymentCancelRequestDto(cancelReason),
                        headers),
                PaymentCompleteResponseDto.class
        );
        // if request fail
        if(response.getStatusCode() != HttpStatus.OK) throw new PaymentFailException("cancel Request to toss 실패", PaymentError.FAIL_CANCEL_REQUEST_TO_TOSS, response.getBody());

        PaymentCompleteResponseDto body = response.getBody();
        if(body.getStatus().equals(PaymentResultStatus.CANCELED.getStatus())){

            //Save cancel status
            order.refund();
            //Cancel payment
            order.getPayment().cancel(body.getApprovedAt());
        }
        else throw new OrderFailException("취소 요청은 정상이지만 파라미터 때문에 실패", OrderError.FAIL_PAYMENT_CANCEL);

        return new PaymentUserCancelResponse(body.getApprovedAt());
    }
}
