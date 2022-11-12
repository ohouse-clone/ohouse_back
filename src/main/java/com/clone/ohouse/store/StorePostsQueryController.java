package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.StorePostsQueryService;
import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategorySearch;
import com.clone.ohouse.store.domain.item.ItemSearchCondition;
import com.clone.ohouse.store.domain.item.bed.Bed;
import com.clone.ohouse.store.domain.item.bed.BedColor;
import com.clone.ohouse.store.domain.item.bed.BedSearchCondition;
import com.clone.ohouse.store.domain.item.bed.StorageBed;
import com.clone.ohouse.store.domain.item.itemselector.ItemSelector;
import com.clone.ohouse.store.domain.storeposts.dto.BundleVIewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class StorePostsQueryController {

    private final StorePostsQueryService storePostsQueryService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/store/category")
    public BundleVIewDto getBundleView(@RequestParam MultiValueMap<String, Object> paramMap, Pageable pageable) throws Exception{
        List<Object> categories = paramMap.get("category");
        if(categories.size() <= 0){
            return null;
        }
        CategorySearch categorySearch = parseCategoryString(categories);

        Category category = categoryRepository.findCategory(categorySearch);
        if(category == null){
            return null;
        }
        Optional<Class> type = new ItemSelector().selectTypeFrom(category.getName());
        Class classType = type.orElse(null);
        ItemSearchCondition condition = null;
        if(classType == Bed.class){
            condition = new BedSearchCondition();
            if(paramMap.containsKey("bedcolor")){
                ArrayList<BedColor> bedColors = paramMap.get("bedcolor").stream().map(t -> BedColor.valueOf((String)t)).distinct().collect(Collectors.toCollection(ArrayList<BedColor>::new));
                for(int l = 0; l < bedColors.size(); ++l)
                    ((BedSearchCondition) condition).bedColor[l] = bedColors.get(l);
            }


        }
        else if(classType == StorageBed.class){
        }
        else {
            condition = new ItemSearchCondition();
        }
        return storePostsQueryService.getBundleViewV3(categorySearch, pageable, condition);
    }

    private CategorySearch parseCategoryString(List<Object> category) {
        CategorySearch categorySearch = new CategorySearch(null, null, null, null);

        String categoryCodes = (String) category.get(0);
        String[] s = categoryCodes.split("_");

        if(s.length >= 1) categorySearch.setCode1(Long.parseLong(s[0]));
        if(s.length >= 2) categorySearch.setCode2(Long.parseLong(s[1]));
        if(s.length >= 3) categorySearch.setCode3(Long.parseLong(s[2]));
        if(s.length >= 4) categorySearch.setCode4(Long.parseLong(s[3]));
        System.out.println("codes : " + categorySearch.getCode1() + ", "
        + categorySearch.getCode2() +", "
        + categorySearch.getCode3() + ", "
        + categorySearch.getCode4());

        return categorySearch;
    }

}
