package com.growmighty.lectures.firstday.tangledmonolith.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * [과제 DDD-2] 상품 재고 규칙 테스트 — 뼈대입니다. fail(...) 을 지우고 채우세요.
 */
class ProductStockTest {

    @Test
    @DisplayName("재고 안에서 차감하면 재고가 줄어든다")
    void 재고를_차감한다() {
        fail("TODO: 테스트를 작성하세요");
    }

    @Test
    @DisplayName("재고보다 많이 차감하면 예외가 나고 재고는 그대로다")
    void 재고_부족이면_예외() {
        fail("TODO: 테스트를 작성하세요");
    }

    @Test
    @DisplayName("0 이하의 수량은 차감할 수 없다")
    void 수량이_0_이하면_예외() {
        // 힌트: 지금 Product.decreaseStock(-3) 을 호출하면 재고가 늘어납니다. 고쳐 보세요.
        fail("TODO: 테스트를 작성하세요");
    }
}
