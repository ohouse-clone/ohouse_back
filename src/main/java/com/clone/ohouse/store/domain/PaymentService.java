package com.clone.ohouse.store.domain;

import com.clone.ohouse.store.domain.payment.Payment;
import com.clone.ohouse.store.domain.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;

@RequiredArgsConstructor
@Transactional
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Value("${payments.toss.secret_api_key}")
    private String tossSecretApiKeyForTest;
    @Value("${payments.toss.client_api_key}")
    private String tossClientApiKeyForTest;

    @Value("${payments.toss.card.success_url}")
    private String tossSuccessCallBackUrlForCard;
    @Value("${payments.toss.card.fail_url}")
    private String tossFailCallBackUrlForCard;

    public void save(Payment payment){
        paymentRepository.save(payment);
    }

    public void delete(S)
}
