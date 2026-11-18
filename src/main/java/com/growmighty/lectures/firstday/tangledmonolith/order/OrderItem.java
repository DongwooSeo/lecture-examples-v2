package com.growmighty.lectures.firstday.tangledmonolith.order;

import com.growmighty.lectures.firstday.tangledmonolith.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    private Product product;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    public static OrderItem create(Product product, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.productId = product.getId();
        orderItem.productName = product.getName();
        orderItem.unitPrice = product.getPrice();
        orderItem.quantity = quantity;

        return orderItem;
    }

    void assignOrder(Order order) {
        this.order = order;
    }
}
