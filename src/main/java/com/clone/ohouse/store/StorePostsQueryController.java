package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.StorePostsQueryService;
import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategorySearch;
import com.clone.ohouse.store.domain.item.ItemSearchCondition;
import com.clone.ohouse.store.domain.item.bed.*;
import com.clone.ohouse.store.domain.item.itemselector.ItemSelector;
import com.clone.ohouse.store.domain.storeposts.dto.BundleVIewDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class StorePostsQueryController {

    private final StorePostsQueryService storePostsQueryService;
    private final CategoryRepository categoryRepository;

    @ApiOperation(
            value = "CategoryTab 게시글 조회",
            notes = "검색 조건과 함께 조회합니다. 검색 조건은 QueryParameter입니다.<br>" +
                    "사용할 수 있는 검색 조건은 다음과 같습니다<br> + " +
                    "1.필수조건<br>" +
                    "- Category code XX_XX_XX_XX<br>" +
                    "2.충분조건 (이 조건은 카테고리 코드가 오직 4개일 때만 사용 가능합니다)<br>" +
                    "- Category code == 20_22_20_20 일 경우, bedcolor, bedsize<br>" +
                    "- Category code == 20_22_20_21 일 경우, material<br>" +
                    "<br>" +
                    "    bedcolor는 String이며 다음 중 하나입니다. RED, BLUE, WHITE<br>" +
                    "    bedsize는 String이며 다음 중 하나입니다. MS, S, SS, D, Q, K, LK, CK<br>" +
                    "    material는 String이며 다음 중 하나입니다. WOOD, STEEL, FAKE_LEATHER, FAKE_WOOD<br>",
            code = 200)
    @ApiResponse(code = 500, message = "server error")
    @ResponseStatus(HttpStatus.OK)
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
            if(paramMap.containsKey("bedsize")){
                ArrayList<BedSize> bedSizes = paramMap.get("bedsize").stream().map(t -> BedSize.valueOf((String) t)).distinct().collect(Collectors.toCollection(ArrayList<BedSize>::new));
                for(int l = 0; l < bedSizes.size(); ++l) ((BedSearchCondition) condition).bedSize[l] = bedSizes.get(l);
            }

        }
        else if(classType == StorageBed.class){
            condition = new StorageBedCondition();
            if(paramMap.containsKey("material")){
                ArrayList<Material> materials = paramMap.get("material").stream().map(t -> Material.valueOf((String) t)).distinct().collect(Collectors.toCollection(ArrayList<Material>::new));
                for(int l = 0; l < materials.size(); ++l) ((StorageBedCondition) condition).material[l] = materials.get(l);
            }
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

        return categorySearch;
    }

}
