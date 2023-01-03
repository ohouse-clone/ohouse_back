package com.clone.ohouse.store.domain.payment;

public enum PaymentResultStatus {
    READY("READY"),
    IN_PROGRESS("IN_PROGRESS"),
    WAITING_FOR_DEPOSIT("WAITING_FOR_DEPOSIT"),
    DONE("DONE"),
    CANCELED("CANCELED"),
    PARTIAL_CANCELED("PARTIAL_CANCELED"),
    ABORTED("ABORTED"),
    EXPIRED("EXPIRED");

    private String status;

    PaymentResultStatus(String status) {
        this.status = status;
    }
}
