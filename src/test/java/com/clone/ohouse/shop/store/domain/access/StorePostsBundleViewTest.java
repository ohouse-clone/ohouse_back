package com.clone.ohouse.shop.store.domain.access;


import com.clone.ohouse.shop.order.domain.entity.Order;
import com.clone.ohouse.shop.product.domain.access.BedRepository;
import com.clone.ohouse.shop.product.domain.access.ItemCategoryCodeRepository;
import com.clone.ohouse.shop.product.domain.access.ProductRepository;
import com.clone.ohouse.shop.product.domain.entity.Bed;
import com.clone.ohouse.shop.product.domain.entity.ItemCategoryCode;
import com.clone.ohouse.shop.product.domain.entity.Product;
import com.clone.ohouse.shop.store.domain.entity.StorePosts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class StorePostsBundleViewTest {
    @Autowired
    private ItemCategoryCodeRepository itemCategoryCodeRepository;
    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StorePostsRepository storePostsRepository;

    private ItemCategoryCode savedCategory;
    private Long maxPopular = 0L;

    @BeforeEach
    void setup() {
        String itemName = "침대이름";
        String productName = "제품이름";
        String size = "중";
        String postTitle = "제목";

        savedCategory = itemCategoryCodeRepository.findByCategory1AndCategory2AndCategory3AndCategory4(0, 22, 20, 20);


        for (int i = 0; i < 100; i++) {
            Long pop = 100L - i;
            maxPopular = Math.max(pop, maxPopular);

            StorePosts post = StorePosts.builder()
                    .title(postTitle + Integer.toString(i))
                    .author("JJH")
                    .build();
            Bed bed = Bed.builder()
                    .categoryCode(savedCategory)
                    .name(itemName + Integer.toString(i))
                    .size(size + Integer.toString(i))
                    .build();
            Product product = Product.builder()
                    .productName(productName + Integer.toString(i))
                    .price(1000 + i)
                    .stock(2000 + i)
                    .rateDiscount(i)
                    .popular(pop)
                    .item(bed)
                    .build();

            product.registerStorePosts(post);

            storePostsRepository.save(post);
            bedRepository.save(bed);
            productRepository.save(product);
        }
    }

    @AfterEach
    void clean() {
        productRepository.deleteAll();
        storePostsRepository.deleteAll();
        bedRepository.deleteAll();
        itemCategoryCodeRepository.deleteAll();
    }

    @Test
    @DisplayName("Get StorePosts bundle, categoryCode & popular & page")
    void findBundleViewByCategoryOrderByPopular() {
        ItemCategoryCode c = savedCategory;

        PageRequest request = PageRequest.of(0, 50);
        List<StorePosts> lst = storePostsRepository.findBundleViewByCategoryOrderByPopular(
                c.getCategory1(), c.getCategory2(), c.getCategory3(), c.getCategory4(),
                request
        );

        Assertions.assertThat(lst.get(0).getProductList().get(0).getPopular()).isEqualTo(maxPopular);
        /** View **/
        for (StorePosts storePosts : lst) {
            System.out.println("storePosts title = " + storePosts.getTitle());
            System.out.println("storePosts product popularity = " + storePosts.getProductList().get(0).getPopular());
            System.out.println();
            System.out.println();
        }
    }
}
