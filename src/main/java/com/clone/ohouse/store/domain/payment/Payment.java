package com.clone.ohouse.store.domain.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter

@Entity
public class Payment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_uuid_for_order", length = 255, unique = true)
    private String orderId;
    @Column(length = 255)
    private String paymentKey;

    @Column
    private String buyerName;

    @Enumerated(value = EnumType.STRING)
    private PaymentResultStatus status = PaymentResultStatus.READY;

    @Column
    private String requestedAt;
    @Column
    private String approvedAt;
    @Column
    private Long totalAmount = 0L;
    @Column
    private Long balanceAmount = 0L;


    protected Payment() {
    }

    public static Payment createPayment(String buyerName) {
        Payment payment = new Payment();
        payment.orderId = UUID.randomUUID().toString();
        payment.buyerName = buyerName;
        payment.status = PaymentResultStatus.READY;
        payment.paymentKey = null;
        payment.requestedAt = null;
        payment.approvedAt = null;

        return payment;
    }
    public void cancel(){
        if(status == PaymentResultStatus.DONE){
            status = PaymentResultStatus.CANCELED;
        }
    }

    public void registerPaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }

    public void processPayment(String paymentKey, PaymentResultStatus status, String requestedAt, String approvedAt, Long totalAmount, Long balanceAmount) {
        this.paymentKey = paymentKey;
        this.status = status;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.balanceAmount = balanceAmount;
        this.totalAmount = totalAmount;
    }
}
