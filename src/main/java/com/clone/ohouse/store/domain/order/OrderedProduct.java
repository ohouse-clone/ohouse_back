package com.clone.ohouse.store.domain.order;

import com.clone.ohouse.store.domain.product.Product;
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

    private Long price;
    private Long amount;

    public OrderedProduct(Product product, Order order, Long price, Long amount) {
        this.product = product;
        this.order = order;
        this.price = price;
        this.amount = amount;
    }

    public void cancelOrdered() throws Exception{
        product.returnAmount(amount);
        amount = 0L;
    }
}
