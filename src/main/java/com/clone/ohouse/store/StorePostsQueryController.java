package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.StorePostsQueryService;
import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.category.CategoryParser;
import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategorySearch;
import com.clone.ohouse.store.domain.item.ItemSearchCondition;
import com.clone.ohouse.store.domain.item.bed.*;
import com.clone.ohouse.store.domain.item.digital.*;
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
                    "1번과 2번은 검색을 위한 예제입니다. <br>" +
                    "1.필수조건<br>" +
                    "- category code XX_XX_XX_XX<br>" +
                    "2.충분조건 <br>" +
                    "- category code == 20_22_20_20 일 경우, bedcolor, bedsize<br>" +
                    "- category code == 20_22_20_21 일 경우, material<br>" +
                    "<br><br>" +
                    "같은 복수개로 사용하는 것을 허락합니다. 예를 들어 bedcolor=RED & bedcolor=BLUE 식으로 계속 이어 쓸 수 있습니다. <br>" +
                    "사용할 수 있는 검색 조건은 아래와 같습니다<br><br>" +
                    "category : 이름 : 사용 가능 조건 순입니다.<br>" +
                    "-    20_22_20_20 : 가구-침대-침대프레임-일반침대 : bedcolor(RED, BLUE, WHITE), bedsize(MS, S, SS, D, Q, K, LK, CK) <br>" +
                    "-    20_22_20_21 : 가구-침대-침대프레임-수납침대 : material(WOOD, STEEL, FAKE_LEATHER, FAKE_WOOD),  <br>" +
                    "-    20_35_25_25 : 가구-테이블-책상-일반책상 : deskcolor(BLACK, WHITE, ETC), framematerial(WOOD, GLASS, PLASTIC), usagetype(SITTING, NORMAL, STANDING) <br>" +
                    "-    20_35_26_26 : 가구-테이블-식탁-식탁 : tableshape(SQUARE, RECTANGLE, CIRCLE, ELLIPSE), framematerial(WOOD, GLASS, PLASTIC), numberofuser(P1, P2, P3) <br>" +
                    "-    30_40_30 : 디지털-냉장고-일반냉장고 : capacity( LESS_S50L, S51L_S100L, S101L_MORE) <br>" +
                    "-    30_41_31 : 디지털-세탁기-일반세탁기  : recommendnumber(P1, P2, P3_MORE)  <br>" +
                    "<br><br>" +
                    "더 자세한 정보는 https://github.com/ohouse-clone/ohouse_back/blob/develop/src/main/java/com/clone/ohouse/store/category.txt 를 참조하세요. 한글명칭과 함께 기재했습니다. <br>" +
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
            condition = new DiningTableSearchCondition();
            DiningTableSearchCondition newCon = (DiningTableSearchCondition)condition;
            if(paramMap.containsKey("tableshape")){
                newCon.diningTableShapes = paramMap.get("tableshape").stream().map(t -> DiningTableShape.valueOf((String) t)).distinct().collect(Collectors.toCollection(ArrayList<DiningTableShape>::new));
            }
            if(paramMap.containsKey("framematerial")){
                newCon.frameMaterials = paramMap.get("framematerial").stream().map(t -> FrameMaterial.valueOf((String) t)).distinct().collect(Collectors.toCollection(ArrayList<FrameMaterial>::new));
            }
            if(paramMap.containsKey("numberofuser")){
                newCon.numberOfUsers = paramMap.get("numberofuser").stream().map(t -> NumberOfUsers.valueOf((String) t)).distinct().collect(Collectors.toCollection(ArrayList<NumberOfUsers>::new));
            }
        }
        else if(type == Refrigerator.class){
            condition = new RefrigeratorSearchCondition();
            RefrigeratorSearchCondition newCon = (RefrigeratorSearchCondition)condition;
            if(paramMap.containsKey("capacity")) {
                newCon.refrigeratorCapacities = paramMap.get("capacity").stream().map(t -> RefrigeratorCapacity.valueOf((String) t)).distinct().collect(Collectors.toCollection(ArrayList<RefrigeratorCapacity>::new));
            }
        }
        else if(type == WashingMachine.class){
            condition = new WashingMachineSearchCondition();
            WashingMachineSearchCondition newCon = (WashingMachineSearchCondition) condition;
            if(paramMap.containsKey("recommendnumber")){
                newCon.recommendNumbers = paramMap.get("recommendnumber").stream().map(t -> RecommendNumber.valueOf((String) t)).distinct().collect(Collectors.toCollection(ArrayList<RecommendNumber>::new));
            }
        }
        else {
            condition = new ItemSearchCondition();
        }
        return condition;
    }

}
