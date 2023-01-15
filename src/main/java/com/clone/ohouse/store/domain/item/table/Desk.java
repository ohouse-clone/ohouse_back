package com.clone.ohouse.store.domain.item.table;

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
public class Desk extends Item {
    @Enumerated(value = EnumType.STRING)
    private DeskColor color;

    @Enumerated(value = EnumType.STRING)
    private FrameMaterial frameMaterial;

    @Enumerated(value = EnumType.STRING)
    private UsageType usageType;

    @Builder
    public Desk(String name, String modelName, String brandName, DeskColor color, FrameMaterial frameMaterial, UsageType usageType) {
        super(name, modelName, brandName);
        this.color = color;
        this.frameMaterial = frameMaterial;
        this.usageType = usageType;
    }

    public void update(String name, String modelName, String brandName, DeskColor color, FrameMaterial frameMaterial, UsageType usageType) {
        super.update(name, modelName, brandName);
        this.color = color;
        this.frameMaterial = frameMaterial;
        this.usageType = usageType;
    }
}
