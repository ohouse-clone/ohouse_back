package com.clone.ohouse.community.domain.cardcollections;

public enum HousingType {
    ONE_ROOM("ONE_ROOM"), APARTMENT("APARTMENT"), VILLA("VILLA"), SINGLE_HOUSE("SINGLE_HOUSE"),OFFICE("OFFICE"),COMMERCIAL("COMMERCIAL");

    private final String name;

    HousingType(String name) {
        this.name = name;
    }
}
