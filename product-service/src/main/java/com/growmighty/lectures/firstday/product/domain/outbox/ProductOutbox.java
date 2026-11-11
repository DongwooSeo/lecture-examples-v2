package com.growmighty.lectures.firstday.product.domain.outbox;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * "이 상품은 검색 색인을 다시 해야 한다"는 기록.
 * 상품 변경과 <b>같은 트랜잭션</b>에 저장되므로, 상품이 커밋됐다면 이 기록도 반드시 남는다.
 */
@Entity
@Table(name = "product_outbox", indexes = @Index(name = "idx_outbox_pending", columnList = "processedAt, id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    /** null 이면 아직 색인하지 않은 기록 */
    private LocalDateTime processedAt;

    private ProductOutbox(Long productId) {
        this.productId = productId;
        this.createdAt = LocalDateTime.now();
    }

    public static ProductOutbox pending(Long productId) {
        return new ProductOutbox(productId);
    }

    public void markProcessed() {
        this.processedAt = LocalDateTime.now();
    }
}
