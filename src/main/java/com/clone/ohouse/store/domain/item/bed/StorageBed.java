package com.clone.ohouse.store.domain.item.bed;

import com.clone.ohouse.store.domain.item.Item;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class StorageBed extends Item {
    @Enumerated(value = EnumType.STRING)
    private Material material;


}
