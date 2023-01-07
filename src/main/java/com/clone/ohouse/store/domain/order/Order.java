package com.clone.ohouse.store.domain.order;

import com.clone.ohouse.account.domain.user.User;
import com.clone.ohouse.store.domain.payment.Payment;
import com.clone.ohouse.store.domain.storeposts.StorePosts;
import com.clone.ohouse.store.error.order.OrderError;
import com.clone.ohouse.store.error.order.OrderFailException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Column
    private LocalDateTime fixedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_post_id")
    private StorePosts storePost;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order")
    List<OrderedProduct> orderedProducts = new ArrayList<>();

    public static Order makeOrder(User user, Delivery delivery, Payment payment, StorePosts post, String orderName) throws Exception {
        Order order = new Order();

        order.createTime = LocalDateTime.now();
        order.user = user;
        order.delivery = delivery;
        order.status = OrderStatus.READY;
        order.payment = payment;
        order.name = orderName;
        order.storePost = post;

        return order;
    }

    public void registerOrderProducts(List<OrderedProduct> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    public void successPayment(){
        if(this.status == OrderStatus.READY) this.status = OrderStatus.CHARGED;
    }
    public void cancel() throws Exception {
        if (this.status.equals(OrderStatus.DELIVERY))
            throw new RuntimeException("Fail to cancel order, It's middle on delivery");

        this.status = OrderStatus.CANCEL;
        for (OrderedProduct orderedProduct : orderedProducts) orderedProduct.cancelOrdered();
    }

    public void refund() throws OrderFailException {
        if(this.status != OrderStatus.CHARGED) throw new OrderFailException("changeOrderStatus 실패", OrderError.REFUND_HAVE_TO_CHARGE);

        this.status = OrderStatus.REFUND;
    }

    public long getTotalPrice() {
        long total = 0;
        for (var orderedProduct : orderedProducts) {
            total += orderedProduct.getPrice();
        }

        return total;
    }

    public void changeOrderStatus(OrderStatus status) throws OrderFailException {
        if(status == OrderStatus.READY) throw new OrderFailException("changeOrderStatus 실패", OrderError.STATE_READY_NOT_CHANGED);
        else if(status == OrderStatus.CANCEL) throw new OrderFailException("changeOrderStatus 실패", OrderError.STATE_CANCEL_NOT_CHANGED);
        else if(status == OrderStatus.CHARGED) throw new OrderFailException("changeOrderStatus 실패", OrderError.STATE_CHARGE_NOT_CHANGED);

        this.status = status;
    }
}
