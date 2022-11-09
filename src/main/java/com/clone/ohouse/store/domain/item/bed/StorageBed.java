package com.clone.ohouse.store.domain.item.bed;

import com.clone.ohouse.store.domain.item.Item;
import org.springframework.web.bind.annotation.MatrixVariable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class StorageBed extends Item {
    @Enumerated(value = EnumType.STRING)
    private Material material;

    public StorageBed(String name, String modelName, String brandName, Material material) {
        super(name, modelName, brandName);
        this.material = material;
    }

    public void update(String name, String modelName, String brandName, Material material) {
        super.update(name, modelName, brandName);
        if(material != null) this.material = material;
    }

}
