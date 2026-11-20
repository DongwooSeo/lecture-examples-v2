package com.growmighty.lectures.firstday.tangledmonolith.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Lob
    private String description;

    /** create - 상품 생성 */
    public static Product create(Long sellerId, String name, BigDecimal price, Integer stockQuantity, String description) {
        Product product = new Product();
        product.sellerId = sellerId;
        product.name = name;
        product.price = price;
        product.stockQuantity = stockQuantity;
        product.description = description;
        return product;
    }

    /** decreaseStock - 재고 차감 */
    public void decreaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("0 이하의 수량이 입력됐습니다.");
        }
        if (this.stockQuantity < quantity) {
            throw new IllegalStateException("재고가 부족합니다. product=" + this.name);
        }

        this.stockQuantity = (this.stockQuantity - quantity);
    }
}
