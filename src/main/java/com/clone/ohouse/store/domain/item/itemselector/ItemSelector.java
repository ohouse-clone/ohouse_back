package com.clone.ohouse.store.domain.item.itemselector;

import com.clone.ohouse.store.domain.category.Category;
import lombok.Getter;

import java.util.Objects;
import java.util.Optional;

@Getter
public class ItemSelector {
    private final String furniture = "가구";
    private final String bed = "침대";
    private final String bedFrame = "침대프레임";
    private final String normalBed = "일반침대";
    private final String storageBed = "수납침대";

    public Optional<Class> selectTypeFrom(String categoryName) throws Exception{
        if(normalBed.equals(categoryName)) return Optional.of(Class.forName("Bed"));
        else if(storageBed.equals(categoryName)) return Optional.of(Class.forName("StorageBed"));

        return Optional.empty();
    }
}
