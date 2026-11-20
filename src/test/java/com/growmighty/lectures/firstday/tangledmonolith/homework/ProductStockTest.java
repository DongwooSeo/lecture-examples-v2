package com.growmighty.lectures.firstday.tangledmonolith.homework;

import com.growmighty.lectures.firstday.tangledmonolith.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * [과제 DDD-2] 상품 재고 규칙 테스트 — 뼈대입니다. fail(...) 을 지우고 채우세요.
 */
class ProductStockTest {

    @Test
    @DisplayName("재고 안에서 차감하면 재고가 줄어든다")
    void 재고를_차감한다() {
        // given
        Product product = Product.create(
                10L, "의자", BigDecimal.valueOf(10000), 99, "갈색 의자");

        // when
        product.decreaseStock(50);

        // then
        assertThat(product.getStockQuantity()).isEqualTo(99 - 50);
    }

    @Test
    @DisplayName("재고보다 많이 차감하면 예외가 나고 재고는 그대로다")
    void 재고_부족이면_예외() {
        // given
        Product product = Product.create(
                10L, "의자", BigDecimal.valueOf(10000), 99, "갈색 의자");

        // when & then
        assertThatThrownBy(() -> product.decreaseStock(999))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("재고가 부족합니다. product=" + product.getName());
        // 재고가 그대로임을 확인
        assertThat(product.getStockQuantity()).isEqualTo(99);
    }

    @Test
    @DisplayName("0 이하의 수량은 차감할 수 없다")
    void 수량이_0_이하면_예외() {
        // 힌트: 지금 Product.decreaseStock(-3) 을 호출하면 재고가 늘어납니다. 고쳐 보세요.
        // given
        Product product = Product.create(
                10L, "의자", BigDecimal.valueOf(10000), 99, "갈색 의자");

        // when & then
        assertThatThrownBy(() -> product.decreaseStock(-3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("0 이하의 수량이 입력됐습니다.");
        // 재고가 그대로임을 확인
        assertThat(product.getStockQuantity()).isEqualTo(99);
    }
}
