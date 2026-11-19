package com.growmighty.lectures.firstday.tangledmonolith.order;

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

    // 주문 후 할인, 가격 인하 등으로 판매중인 제품 가격은 변할 수 있으나
    // 사용자의 결제는 되돌릴 수 없으므로 스냅샷으로 조회 필요
    @Column (nullable = false)
    private Long productId;

    @Column (nullable = false)
    private String productName;

    @Column (nullable = false)
    private BigDecimal productPrice;

    @Column(nullable = false)
    private Integer quantity;

    public static OrderItem create(Long productId, String productName, BigDecimal productPrice, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.productId = productId;
        orderItem.productName = productName;
        orderItem.productPrice = productPrice;
        orderItem.quantity = quantity;

        return orderItem;
    }

    void assignOrder(Order order) {
        this.order = order;
    }
}
