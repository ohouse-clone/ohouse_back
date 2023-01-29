package com.clone.ohouse.store.domain.item.digital;


public enum RefrigeratorCapacity {
    LESS_S50L("LESS_S50L"),
    S51L_S100L("S51L_S100L"),
    S101L_MORE("S101L_MORE")
    ;

    private final String capacity;

    RefrigeratorCapacity(String capacity) {
        this.capacity = capacity;
    }
}
