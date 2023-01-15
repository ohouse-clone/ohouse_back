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
public class WashingMachine extends Item {
    @Enumerated(value = EnumType.STRING)
    private RecommendNumber recommendNumber;

    @Builder
    public WashingMachine(String name, String modelName, String brandName, RecommendNumber recommendNumber) {
        super(name, modelName, brandName);
        this.recommendNumber = recommendNumber;
    }

    public void update(String name, String modelName, String brandName, RecommendNumber recommendNumber) {
        super.update(name, modelName, brandName);
        this.recommendNumber = recommendNumber;
    }
}

