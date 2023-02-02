package com.clone.ohouse.error.order;

import lombok.Getter;

@Getter
public class OrderFailException extends RuntimeException {
    private final OrderError orderError;
    public OrderFailException(String message, OrderError orderError) {
        super("ErrorCode=" + orderError.name() + ", " +message);
        this.orderError = orderError;
    }


    public OrderError getOrderError() {
        return orderError;
    }
}
