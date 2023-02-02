package com.clone.ohouse.store.domain.item.bed;

import io.swagger.annotations.ApiModel;

@ApiModel(
        value = "BedSize"
)
public enum BedSize {
    MS("MS"), S("S"), SS("SS"), D("D"), Q("Q"), K("K"), LK("LK"), CK("CK");

    private final String name;

    BedSize(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
