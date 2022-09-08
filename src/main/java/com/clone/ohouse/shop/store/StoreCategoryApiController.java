package com.clone.ohouse.shop.store;

import com.clone.ohouse.shop.product.domain.access.ItemCategoryCodeRepository;
import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import com.clone.ohouse.shop.store.domain.StoreCategoryService;
import com.clone.ohouse.shop.store.domain.StorePostsService;
import com.clone.ohouse.shop.store.domain.dto.StorePostsBundleViewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoreCategoryApiController {

    private final ItemCategoryCodeRepository itemCategoryCodeRepository;
    private final StoreCategoryService storeCategoryService;

    @GetMapping("/store/category")
    public List<StorePostsBundleViewResponseDto> getCategoryView(
            @RequestParam String category,
            @RequestParam String order,
            @RequestParam Integer page,
            @RequestParam Integer per) {

        List<StorePostsBundleViewResponseDto> bundle;
        ItemCategoryCode code = null;
        Pageable pageable = PageRequest.of(page, per);

        String[] s = category.split("_");
        if(s.length == 4){
            code = itemCategoryCodeRepository.findByCategory1AndCategory2AndCategory3AndCategory4(Integer.valueOf(s[0]), Integer.valueOf(s[1]), Integer.valueOf(s[2]), Integer.valueOf(s[3]));
        }

        bundle = storeCategoryService.findBundleViewAllOrderByPopular(code, pageable);

        return bundle;
    }
}
