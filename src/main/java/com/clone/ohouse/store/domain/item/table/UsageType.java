package com.clone.ohouse.store.domain.item.table;


public enum UsageType {
    SITTING("SITTING"),
    NORMAL("NORMAL"),
    STANDING("STANDING")
    ;


    private final String type;

    UsageType(String type) {
        this.type = type;
    }
}
