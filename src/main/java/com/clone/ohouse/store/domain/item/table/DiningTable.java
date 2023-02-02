package com.clone.ohouse.store.domain.item.table;


import com.clone.ohouse.store.domain.item.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@NoArgsConstructor
@Entity
public class DiningTable extends Item {

    @Enumerated(value = EnumType.STRING)
    private DiningTableShape tableShape;

    @Enumerated(value = EnumType.STRING)
    private FrameMaterial frameMaterial;

    @Enumerated(value = EnumType.STRING)
    private NumberOfUsers numberOfUsers;

    @Builder
    public DiningTable(String name, String modelName, String brandName, DiningTableShape tableShape, FrameMaterial frameMaterial, NumberOfUsers numberOfUsers) {
        super(name, modelName, brandName);
        this.tableShape = tableShape;
        this.frameMaterial = frameMaterial;
        this.numberOfUsers = numberOfUsers;
    }


    public void update(String name, String modelName, String brandName, DiningTableShape tableShape, FrameMaterial frameMaterial, NumberOfUsers numberOfUsers) {
        super.update(name, modelName, brandName);
        if(tableShape != null) this.tableShape = tableShape;
        if(frameMaterial != null) this.frameMaterial = frameMaterial;
        if(numberOfUsers != null) this.numberOfUsers = numberOfUsers;
    }
}
