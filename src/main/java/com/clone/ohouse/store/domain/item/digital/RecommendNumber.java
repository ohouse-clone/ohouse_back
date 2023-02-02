package com.clone.ohouse.store.domain.item.digital;

public enum RecommendNumber {
    P1("P1"),
    P2("P2"),
    P3_P4("P3_MORE")
    ;

    private final String number;

    RecommendNumber(String number) {
        this.number = number;
    }
}
