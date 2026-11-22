package com.growmighty.lectures.firstday.tangledmonolith.homework;

import com.growmighty.lectures.firstday.tangledmonolith.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductStockTest {

    @Test
    @DisplayName("재고 안에서 차감하면 재고가 줄어든다")
    void 재고를_차감한다() {
        Product product = Product.create(1L, "Seller", BigDecimal.valueOf(100000), 10, "description");
        product.decreaseStock(5);
        assertThat(product.getStockQuantity() == 5);
    }

    @Test
    @DisplayName("재고보다 많이 차감하면 예외가 나고 재고는 그대로다")
    void 재고_부족이면_예외() {
        Product product = Product.create(1L, "Seller", BigDecimal.valueOf(100000), 5, "description");
        assertThatThrownBy(() -> product.decreaseStock(10)).isInstanceOf(IllegalStateException.class);
        assertThat(product.getStockQuantity() == 5);
    }

    @Test
    @DisplayName("0 이하의 수량은 차감할 수 없다")
    void 수량이_0_이하면_예외() {
        // 힌트: 지금 Product.decreaseStock(-3) 을 호출하면 재고가 늘어납니다. 고쳐 보세요.
        Product product = Product.create(1L, "Seller", BigDecimal.valueOf(100000), 5, "description");
        assertThatThrownBy(() -> product.decreaseStock(-5)).isInstanceOf(IllegalArgumentException.class);
        assertThat(product.getStockQuantity() == 5);    }
}
