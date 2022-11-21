package com.clone.ohouse.store;

import com.clone.ohouse.store.domain.ItemService;
import com.clone.ohouse.store.domain.category.Category;
import com.clone.ohouse.store.domain.category.CategoryParser;
import com.clone.ohouse.store.domain.category.CategoryRepository;
import com.clone.ohouse.store.domain.category.CategorySearch;
import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.item.ItemRequestDto;
import com.clone.ohouse.store.domain.item.ItemSearchCondition;
import com.clone.ohouse.store.domain.item.bed.dto.BedRequestDto;
import com.clone.ohouse.store.domain.item.bed.dto.StorageBedRequestDto;
import com.clone.ohouse.store.domain.item.dto.ItemBundleViewDto;
import com.clone.ohouse.store.domain.item.itemselector.ItemSelector;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(
        value = "Item 등록, 제거, 찾기에 대한 API<br><br>" +
        "<h1> save interface </h1>" +
        "save api는 카테고리의 의존적이라는 중요한 특징을 가집니다.<br>" +
        "카테고리는 iteml의 세부항목을 결정짓는 중요한 parameter이며<br>" +
        "저장하려는 DTO와 category code는 사전에 static하게 연관되어 있습니다.<br>" +
        "따라서 정확하게 category와 dto를 사용해야 합니다.<br>" +
        "예를 들어서 category 20_22_20_20은 bed를 의미하므로 BedRequestDto를 사용하며, post /store/item/bed 로 하여야 합니다."
)
@RequiredArgsConstructor
@RequestMapping("/store/api/v1/item")
@RestController
public class ItemApiController {
    private final ItemService itemService;
    private final CategoryRepository categoryRepository;


    @ApiOperation(
            value = "item(bed) 저장",
            notes = "카테고리와 함께 사용됩니다.",
            code = 201
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/bed")
    public HttpEntity<Long> save(
            @ApiParam(value = "category code") @RequestParam String category,
            @RequestBody BedRequestDto requestDto) throws Exception {
        return saveWithValidate(category, requestDto);
    }

    @ApiOperation(
            value = "item(storagebed) 저장",
            notes = "카테고리와 함께 사용됩니다.",
            code = 201
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/storagebed")
    public HttpEntity<Long> save(
            @ApiParam(value = "category code") @RequestParam String category,
            @RequestBody StorageBedRequestDto requestDto) throws Exception {
        return saveWithValidate(category, requestDto);
    }

    @ApiOperation(
            value = "등록된 item 제거",
            code = 200
    )
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(
            @ApiParam(value = "item id") @PathVariable Long id) throws Exception {
        itemService.delete(id);
    }

    @ApiOperation(
            value = "저장된 items 보기",
            code  = 200
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/items")
    public HttpEntity<ItemBundleViewDto> findByCategory(
            @ApiParam(value = "category code") @RequestParam String category,
            @ApiParam(required = false) Pageable pageable,
            @ApiParam(required = false) @RequestParam(required = false) String itemName,
            @ApiParam(required = false) @RequestParam(required = false) String itemModelName,
            @ApiParam(required = false) @RequestParam(required = false) String itemBrandName) throws Exception {
        CategorySearch categorySearch = CategoryParser.parseCategoryString(category);
        ItemSearchCondition itemSearchCondition = new ItemSearchCondition();
        itemSearchCondition.itemName = itemName;
        itemSearchCondition.modelName = itemModelName;
        itemSearchCondition.brandName = itemBrandName;

        if (categorySearch.getCode1() == null || categorySearch.getCode2() == null || categorySearch.getCode3() == null || categorySearch.getCode4() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        // itemSearchCondition 중 1가지만 null이 아니거나, 모두 null이어야 함
        if (!validateConditionIsOnlyOne(itemSearchCondition)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(itemService.findByCategory(categorySearch, pageable, itemSearchCondition), HttpStatus.OK);
    }
    private boolean validateConditionIsOnlyOne(ItemSearchCondition itemSearchCondition){
        int count = 0;
        if(StringUtils.hasText(itemSearchCondition.itemName)) count++;
        if(StringUtils.hasText(itemSearchCondition.brandName)) count++;
        if(StringUtils.hasText(itemSearchCondition.modelName)) count++;

        if(count > 1) return false;

        return true;
    }

    private HttpEntity<Long> saveWithValidate(String category, ItemRequestDto requestDto) throws Exception {
        //TODO: categoryRepository로 찾는 로직은 Service 안으로 들어가야함, 추후 Fix 예정
        CategorySearch categorySearch = CategoryParser.parseCategoryString(category);
        Category categoryEntity = categoryRepository.findCategory(categorySearch);
        Item item = requestDto.toEntity();
        Class type = new ItemSelector().selectTypeFrom(categoryEntity.getName()).orElse(null);

        if (categoryEntity == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (categorySearch.getCode4() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (type == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (item.getClass() != type) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


        Long savedId = itemService.save(item, categorySearch);
        return new ResponseEntity<Long>(savedId, HttpStatus.CREATED);
    }

}
