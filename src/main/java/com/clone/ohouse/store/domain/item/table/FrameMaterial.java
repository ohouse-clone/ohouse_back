package com.clone.ohouse.store.domain.item.table;

public enum FrameMaterial {
    WOOD("WOOD"),
    GLASS("GLASS"),
    PLASTIC("PLASTIC")
    ;

    private final String material;

    FrameMaterial(String material) {
        this.material = material;
    }
}
