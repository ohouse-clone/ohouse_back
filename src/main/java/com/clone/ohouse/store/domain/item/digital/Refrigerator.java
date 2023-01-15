package com.clone.ohouse.store.domain.item.digital;

import com.clone.ohouse.store.domain.item.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@Getter
@Entity
public class Refrigerator extends Item {
    @Enumerated(value = EnumType.STRING)
    private RefrigeratorCapacity capacity;

    @Builder
    public Refrigerator(String name, String modelName, String brandName, RefrigeratorCapacity capacity) {
        super(name, modelName, brandName);
        this.capacity = capacity;
    }

    void update(String name, String modelName, String brandName, RefrigeratorCapacity capacity) {
        super.update(name, modelName, brandName);
        this.capacity = capacity;
    }
}
