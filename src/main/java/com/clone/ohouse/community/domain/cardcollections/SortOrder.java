package com.clone.ohouse.community.domain.cardcollections;

public enum SortOrder {
    HIT_ASCEND("HIT_ASCEND"), HIT_DESCEND("HIT_DESCEND");

    private String order;

    SortOrder(String order) {
        this.order = order;
    }
}
