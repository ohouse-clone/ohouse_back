package com.clone.ohouse.store.domain.item.bed;

import com.clone.ohouse.store.domain.item.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@Getter
@Entity
public class Bed extends Item {
    @Enumerated(value = EnumType.STRING)
    private BedSize size;
    @Column(length = 30)
    private String color;


    @Builder
    public Bed(String name, String modelName, String brandName, BedSize size, String color) {
        super(name, modelName, brandName);

        this.size = size;
        this.color = color;
    }

    public void update(String name, String modelName, String brandName, BedSize size, String color) {
        super.update(name, modelName, brandName);
        if (size != null) this.size = size;
        if (color != null) this.color = color;
    }
}
