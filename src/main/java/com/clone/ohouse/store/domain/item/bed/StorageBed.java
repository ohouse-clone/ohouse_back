package com.clone.ohouse.store.domain.item.bed;

import com.clone.ohouse.store.domain.item.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.MatrixVariable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@Getter
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
