package com.clone.ohouse.store.domain.item.bed;

import io.swagger.annotations.ApiModel;

@ApiModel(
        value = "BedColor"
)
public enum BedColor {
    RED("RED"), BLUE("BLUE"), WHITE("WHITE");
    private final String color;

    BedColor(String color) {
        this.color = color;
    }
}
