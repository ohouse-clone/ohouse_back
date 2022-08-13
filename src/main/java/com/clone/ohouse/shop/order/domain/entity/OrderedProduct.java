package com.clone.ohouse.shop.order.domain.entity;

import com.clone.ohouse.shop.product.domain.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class OrderedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer price;
    private Integer amount;

    public OrderedProduct(Product product, Order order, Integer price, Integer amount) {
        this.product = product;
        this.order = order;
        this.price = price;
        this.amount = amount;
    }

    public void cancelOrdered() throws Exception{
        product.returnAmount(amount);
        amount = 0;
    }
}
