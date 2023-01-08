package com.clone.ohouse.store.error.order;

public class PaymentFailException extends RuntimeException{
    private final PaymentError paymentError;
    private Object tossFailObject;

    public PaymentFailException(String message, PaymentError paymentError, Object failObject) {
        super("ErrorCode=" + paymentError.name() + ", " +message);
        this.paymentError = paymentError;
        this.tossFailObject = failObject;
    }

    public PaymentError getPaymentError() {
        return paymentError;
    }

    public Object getTossFailObject() {
        return tossFailObject;
    }
}
