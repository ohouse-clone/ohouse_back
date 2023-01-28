package com.clone.ohouse.store.domain.item.itemselector;

import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.item.bed.Bed;
import com.clone.ohouse.store.domain.item.bed.StorageBed;
import com.clone.ohouse.store.domain.item.digital.Refrigerator;
import com.clone.ohouse.store.domain.item.digital.WashingMachine;
import com.clone.ohouse.store.domain.item.table.Desk;
import com.clone.ohouse.store.domain.item.table.DiningTable;
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

    private final String desk = "일반책상";
    private final String diningTable = "식탁";
    private final String refrigerator = "일반냉장고";
    private final String washingMachine = "일반세탁기";


    public Optional<Class> selectTypeFrom(String categoryName) throws Exception {
        //if(normalBed.equals(categoryName)) return Optional.of(Class.forName("com.clone.ohouse.store.domain.item.bed.Bed"));
        //else if(storageBed.equals(categoryName)) return Optional.of(Class.forName("com.clone.ohouse.store.domain.item.bed.StorageBed"));
        if (normalBed.equals(categoryName)) return Optional.of(Bed.class);
        else if (storageBed.equals(categoryName)) return Optional.of(StorageBed.class);
        else if (desk.equals(categoryName)) return Optional.of(Desk.class);
        else if (diningTable.equals(categoryName)) return Optional.of(DiningTable.class);
        else if (refrigerator.equals(categoryName)) return Optional.of(Refrigerator.class);
        else if (washingMachine.equals(categoryName)) return Optional.of(WashingMachine.class);

        return Optional.empty();
    }
}
