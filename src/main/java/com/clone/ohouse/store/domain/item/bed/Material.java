package com.clone.ohouse.store.domain.item.bed;

import io.swagger.annotations.ApiModel;

@ApiModel(
        value = "StorageBed Material"
)
public enum Material {
    WOOD("WOOD"), STEEL("STEEL"), FAKE_LEATHER("FAKE_LEATHER"), FAKE_WOOD("FAKE_WOOD");

    private final String name;

    Material(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
