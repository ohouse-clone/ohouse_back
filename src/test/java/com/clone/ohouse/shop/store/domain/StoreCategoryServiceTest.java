package com.clone.ohouse.shop.store.domain;

import com.clone.ohouse.shop.product.domain.access.BedRepository;
import com.clone.ohouse.shop.product.domain.access.ItemCategoryCodeRepository;
import com.clone.ohouse.shop.product.domain.access.ProductRepository;
import com.clone.ohouse.shop.product.domain.entity.Bed;
import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import com.clone.ohouse.shop.product.domain.entity.Product;
import com.clone.ohouse.shop.store.domain.access.StorePostsRepository;
import com.clone.ohouse.shop.store.domain.dto.StorePostsBundleViewResponseDto;
import com.clone.ohouse.shop.store.domain.dto.StorePostsSaveRequestDto;
import com.clone.ohouse.shop.store.domain.entity.StorePosts;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class StoreCategoryServiceTest {

    @Autowired
    private StorePostsRepository storePostsRepository;
    @Autowired
    private StorePostsService storePostsService;
    @Autowired
    private StoreCategoryService storeCategoryService;
    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ItemCategoryCodeRepository itemCategoryCodeRepository;

    @AfterEach
    public void clean() {
        storePostsRepository.deleteAll();
    }

    @Test
    public void findBundleViewAllOrderByPopular() {
        //given
        ItemCategoryCode code = itemCategoryCodeRepository.findByCategory1AndCategory2AndCategory3AndCategory4(0, 22, 20, 20);
        StorePosts post1 = storePostsRepository.save(new StorePostsSaveRequestDto("제목1", "내용1", "JJH").toEntity());
        StorePosts post2 = storePostsRepository.save(new StorePostsSaveRequestDto("제목2", "내용2", "AAA").toEntity());
        StorePosts post3 = storePostsRepository.save(new StorePostsSaveRequestDto("제목3", "내용3", "BBB").toEntity());
        StorePosts post4 = storePostsRepository.save(new StorePostsSaveRequestDto("제목4", "내용4", "CCC").toEntity());

        Bed bed1 = bedRepository.save(Bed.builder().categoryCode(code).name("침대1").size("크기1").build());
        Bed bed2 = bedRepository.save(Bed.builder().categoryCode(code).name("침대2").size("크기2").build());
        Bed bed3 = bedRepository.save(Bed.builder().categoryCode(code).name("침대3").size("크기3").build());
        Bed bed4 = bedRepository.save(Bed.builder().categoryCode(code).name("침대4").size("크기4").build());

        Product product1 = Product.builder().productName("제품1").item(bed1).price(1000).build();
        Product product2 = Product.builder().productName("제품2").item(bed2).price(2000).build();
        Product product3 = Product.builder().productName("제품3").item(bed3).price(3000).build();
        Product product4 = Product.builder().productName("제품4").item(bed4).price(4000).build();

        product1.registerStorePosts(post1);
        product2.registerStorePosts(post2);
        product3.registerStorePosts(post3);
        product4.registerStorePosts(post4);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);

        PageRequest request = PageRequest.of(0, 4);

        //when
        List<StorePostsBundleViewResponseDto> bundleView = storeCategoryService.findBundleViewAllOrderByPopular(code, request);

        //then
        Assertions.assertThat(bundleView).extracting(StorePostsBundleViewResponseDto::getTitle)
                .containsExactlyInAnyOrder("제목1", "제목2", "제목3", "제목4");
        Assertions.assertThat(bundleView).extracting(StorePostsBundleViewResponseDto::getPrice)
                .containsExactlyInAnyOrder(1000, 2000, 3000, 4000);
        /* View */
        System.out.println();
        System.out.println(bundleView.size());
        for (StorePostsBundleViewResponseDto dto : bundleView) {
            System.out.println("title = " + dto.getTitle() + " , price = " + dto.getPrice());
        }
    }


}
