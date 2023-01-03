package com.clone.ohouse.store.domain.order;

import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.store.domain.order.dto.OrderedProductDto;
import com.clone.ohouse.store.domain.payment.Payment;
import com.clone.ohouse.store.domain.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "orders")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private LocalDateTime createTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order")
    List<OrderedProduct> orderedProducts = new ArrayList<>();

    public static Order makeOrder(User user, Delivery delivery, Payment payment, String orderName) throws Exception {
        Order order = new Order();

        order.createTime = LocalDateTime.now();
        order.user = user;
        order.delivery = delivery;
        order.status = OrderStatus.READY;
        order.payment = payment;
        order.name = orderName;

        return order;
    }
    public void registerOrderProducts(List<OrderedProduct> orderedProducts){
        this.orderedProducts = orderedProducts;
    }

    public void cancel() throws Exception {
        if (this.status.equals(OrderStatus.DELIVERY))
            throw new RuntimeException("Fail to cancel order, It's middle on delivery");

        this.status = OrderStatus.CANCEL;
        for (OrderedProduct orderedProduct : orderedProducts) orderedProduct.cancelOrdered();

    }

    public void fail() throws Exception {
        if(this.status != OrderStatus.READY) throw new RuntimeException("주문 상태가 ready가 아님으로 실패할 수 없음");

        this.status = OrderStatus.FAIL;
    }

    public long getTotalPrice() {
        long total = 0;
        for (var orderedProduct : orderedProducts) {
            total += orderedProduct.getPrice();
        }

        return total;
    }
}
