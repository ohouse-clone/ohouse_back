package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.StorePostsQueryService;
import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.category.CategoryParser;
import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategorySearch;
import com.clone.ohouse.store.domain.item.ItemSearchCondition;
import com.clone.ohouse.store.domain.item.bed.*;
import com.clone.ohouse.store.domain.item.digital.Refrigerator;
import com.clone.ohouse.store.domain.item.digital.WashingMachine;
import com.clone.ohouse.store.domain.item.itemselector.ItemSelector;
import com.clone.ohouse.store.domain.item.table.*;
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

@Api(
        value = "등록된 게시글 조회 API"
)
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
                    "- category code XX_XX_XX_XX<br>" +
                    "2.충분조건 (이 조건은 카테고리 코드가 오직 4개일 때만 사용 가능합니다)<br>" +
                    "- category code == 20_22_20_20 일 경우, bedcolor, bedsize<br>" +
                    "- category code == 20_22_20_21 일 경우, material<br>" +
                    "<br>" +
                    "    bedcolor는 String이며 다음 중 하나입니다. RED, BLUE, WHITE<br>" +
                    "    bedsize는 String이며 다음 중 하나입니다. MS, S, SS, D, Q, K, LK, CK<br>" +
                    "    material는 String이며 다음 중 하나입니다. WOOD, STEEL, FAKE_LEATHER, FAKE_WOOD<br>" +
            "Response : BundleViewDto",
            code = 200)
    @ApiResponse(code = 500, message = "server error")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/store/category")
    public BundleVIewDto getBundleView(@RequestParam MultiValueMap<String, Object> paramMap, Pageable pageable) throws Exception{
        List<Object> categories = paramMap.get("category");
        if(categories.size() <= 0){
            return null;
        }

        CategorySearch categorySearch = CategoryParser.parseCategoryString((String) categories.get(0));

        Category category = categoryRepository.findCategory(categorySearch);
        if(category == null){
            return null;
        }
        ItemSearchCondition condition = getItemSearchCondition(paramMap, category);
        return storePostsQueryService.getBundleViewV3(categorySearch, pageable, condition);
    }

    private ItemSearchCondition getItemSearchCondition(MultiValueMap<String, Object> paramMap, Category category) throws Exception {
        Class type =  new ItemSelector().selectTypeFrom(category.getName()).orElse(null);
        ItemSearchCondition condition = null;

        if(type == Bed.class){
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
        else if(type == StorageBed.class){
            condition = new StorageBedCondition();
            if(paramMap.containsKey("material")){
                ArrayList<Material> materials = paramMap.get("material").stream().map(t -> Material.valueOf((String) t)).distinct().collect(Collectors.toCollection(ArrayList<Material>::new));
                for(int l = 0; l < materials.size(); ++l) ((StorageBedCondition) condition).material[l] = materials.get(l);
            }
        }
        else if(type == Desk.class){
            condition = new DeskSearchCondition();
            DeskSearchCondition newCon = (DeskSearchCondition)condition;
            if(paramMap.containsKey("deskcolor")){
                newCon.deskColors = paramMap.get("deskcolor").stream().map(t -> DeskColor.valueOf((String) t)).distinct().collect(Collectors.toCollection(ArrayList<DeskColor>::new));
            }
            if(paramMap.containsKey("framematerial")){
                newCon.frameMaterials = paramMap.get("framematerial").stream().map(t -> FrameMaterial.valueOf((String) t)).distinct().collect(Collectors.toCollection(ArrayList<FrameMaterial>::new));
            }
            if(paramMap.containsKey("usagetype")){
                newCon.usageTypes = paramMap.get("usagetype").stream().map(t -> UsageType.valueOf((String) t)).distinct().collect(Collectors.toCollection(ArrayList<UsageType>::new));
            }
        }
        else if(type == DiningTable.class){

        }
        else if(type == Refrigerator.class){

        }
        else if(type == WashingMachine.class){

        }
        else {
            condition = new ItemSearchCondition();
        }
        return condition;
    }

}
