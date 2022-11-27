package com.clone.ohouse.store.domain.product;

import com.clone.ohouse.store.domain.item.Item;
import com.clone.ohouse.store.domain.storeposts.StorePosts;
import com.clone.ohouse.store.domain.order.Order;
import com.clone.ohouse.store.domain.order.OrderedProduct;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(length = 45)
    private String productName;
    private Integer price;
    private Integer stock;
    private Integer rateDiscount;

    private Long popular = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_posts_id")
    private StorePosts storePosts;

    @Builder
    public Product(Item item, String productName, Integer price, Integer stock, Integer rateDiscount, Long popular, StorePosts storePosts) {
        this.item = item;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.rateDiscount = rateDiscount;
        this.popular = popular;
        this.storePosts = storePosts;
    }

    public void GainPopular() {
        this.popular++;
    }

    public void update(Item item, String productName, Integer stock, Integer price, Integer rateDiscount,StorePosts storePosts) {
        if(item != null) this.item = item;
        if(productName != null) this.productName = productName;
        if(price != null) this.price = price;
        if(stock != null) this.stock = stock;
        if(rateDiscount != null) this.rateDiscount = rateDiscount;
        if(storePosts != null) this.storePosts = storePosts;


    }

    public void returnAmount(Integer count) throws Exception{
        if(count <= 0) throw new RuntimeException("물품의 0이하의 수량을 되돌려 줄 수 없습니다.");
        this.stock += count;
    }

    public OrderedProduct makeOrderedProduct(Order order, Integer price, Integer amount) throws Exception{
        if(amount > this.stock) throw new RuntimeException("재고보다 많이 주문할 수 없습니다");

        this.stock -= amount;

        return new OrderedProduct(this, order, price, amount);
    }

    public void registerStorePosts(StorePosts post){
        this.storePosts = post;
        post.getProductList().add(this);
    }

}
