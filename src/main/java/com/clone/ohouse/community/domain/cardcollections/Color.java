package com.clone.ohouse.community.domain.cardcollections;

import io.swagger.annotations.ApiModel;

@ApiModel(
        value = "Color"
)
public enum Color {
    WHITE("WHITE"), BLACK("BLACK"), BLUE("BLUE"), RED("RED");
    private final String name;

    Color(String name) {
        this.name = name;
    }
}
