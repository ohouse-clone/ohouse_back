package com.clone.ohouse.store.domain.item.table;


public enum DiningTableShape {
    SQUARE("SQUARE"),
    RECTANGLE("RECTANGLE"),
    CIRCLE("CIRCLE"),
    ELLIPSE("ELLIPSE"),
    ETC("ETC");
    private final String shape;

    DiningTableShape(String shape) {
        this.shape = shape;
    }
}
