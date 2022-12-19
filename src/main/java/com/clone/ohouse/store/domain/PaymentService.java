package com.clone.ohouse.store.domain;

import com.clone.ohouse.store.domain.order.Order;
import com.clone.ohouse.store.domain.order.OrderRepository;
import com.clone.ohouse.store.domain.payment.Payment;
import com.clone.ohouse.store.domain.payment.PaymentRepository;
import com.clone.ohouse.store.domain.payment.dto.PaymentCompleteRequestDto;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.persistence.Transient;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

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

    public Long save(Payment payment){
        return paymentRepository.save(payment).getId();
    }

    public void delete(Long paymentId){
        paymentRepository.deleteById(paymentId);
    }

    public void verifyPaymentComplete(String paymentKey, String orderApprovalCode, Long amount) throws Exception{
        Payment payment = paymentRepository.findByOrderApprovalCode(orderApprovalCode).orElseThrow(() -> new RuntimeException("Can't find payment from orderApprovalCode : " + orderApprovalCode));
        Order order = orderRepository.findByPayment(payment).orElseThrow(() -> new RuntimeException("Can't find order from payment id : " + payment.getId()));

        if(!payment.getPaymentKey().equals(paymentKey)) throw new RuntimeException("Wrong paymentKey : " + paymentKey);
        if(!order.getTotalPrice().equals(amount.intValue())) throw new RuntimeException("Wrong amount : " + amount);
    }

    public PaymentCompleteRequestDto requestPaymentComplete(String paymentKey, String orderApprovalCode, Long amount){
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //TODO: 결제요청
//        String auth = new String(Base64.getEncoder().encode(tossSecretApiKeyForTest.getBytes(StandardCharsets.UTF_8));
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//
//        headers.setBasicAuth(auth);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        body.add("orderId", orderApprovalCode);
//        body.add("amount", amount.toString());
//
//        template.postForEntity(
//                "https://api.tosspayments.com/v1/payments/confirm \"
//        )
    }
}
