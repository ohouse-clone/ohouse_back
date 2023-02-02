package com.clone.ohouse.community.domain.cardcollections;

import io.swagger.annotations.ApiModel;

@ApiModel(
        value = "HouseStyle"
)
public enum HouseStyle {
    MODERN("MODERN"), NORTH_EUROPE("NORTH_EUROPE"), VINTAGE("VINTAGE"), ROMANTIC("ROMANTIC"), KOREAN_ASIA("KOREA_ASIA"), UNIQUE("UNIQUE"), CLASSIC("CLASSIC");

    private final String name;

    HouseStyle(String name) {
        this.name = name;
    }
}
