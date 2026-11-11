package com.growmighty.lectures.firstday.tangledmonolith.homework;

import com.growmighty.lectures.firstday.tangledmonolith.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** [과제 DDD-2 정답 예시] 상품 재고 규칙 테스트 */
class ProductStockTest {

    private Product productWithStock(int stock) {
        return Product.create(1L, "의자", BigDecimal.valueOf(10000), stock, "설명");
    }

    @Test
    @DisplayName("재고 안에서 차감하면 재고가 줄어든다")
    void 재고를_차감한다() {
        Product product = productWithStock(10);

        product.decreaseStock(3);

        assertThat(product.getStockQuantity()).isEqualTo(7);
    }

    @Test
    @DisplayName("재고보다 많이 차감하면 예외가 나고 재고는 그대로다")
    void 재고_부족이면_예외() {
        Product product = productWithStock(2);

        assertThatThrownBy(() -> product.decreaseStock(3)).isInstanceOf(IllegalStateException.class);
        assertThat(product.getStockQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("0 이하의 수량은 차감할 수 없다")
    void 수량이_0_이하면_예외() {
        Product product = productWithStock(10);

        assertThatThrownBy(() -> product.decreaseStock(-3)).isInstanceOf(IllegalArgumentException.class);
        assertThat(product.getStockQuantity()).isEqualTo(10);
    }
}
