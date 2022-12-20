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

    @Column(length = 1024, unique = true)
    private String orderId;
    @Column(length = 1024)
    private String paymentKey;

    @Column
    private String buyerName;

    @Column
    private boolean isSuccess;

    @Column
    private LocalDateTime completeTime;

    private Payment(){}

    public static Payment createPayment(String buyerName){
        Payment payment = new Payment();
        payment.orderId = UUID.randomUUID().toString();
        payment.buyerName = buyerName;
        payment.isSuccess = false;
        payment.completeTime = null;
        payment.paymentKey = null;

        return payment;
    }

    public void cancel(){
        isSuccess = false;
    }

    public void registerPaymentKey(String paymentKey){
        this.paymentKey = paymentKey;
    }
}
