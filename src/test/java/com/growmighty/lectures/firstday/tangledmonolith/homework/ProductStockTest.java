package com.growmighty.lectures.firstday.tangledmonolith.homework;

import com.growmighty.lectures.firstday.tangledmonolith.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * [과제 DDD-2] 상품 재고 규칙 테스트 — 뼈대입니다. fail(...) 을 지우고 채우세요.
 */
class ProductStockTest {

    @Test
    @DisplayName("재고 안에서 차감하면 재고가 줄어든다")
    void 재고를_차감한다() {
        // given: 재고 10개인 상품 생성 - (Long sellerId, String name, BigDecimal price, Integer stockQuantity, String description)
        Product product = Product.create(153L, "키보드", BigDecimal.valueOf(125000), 10, "그루브 스톤 텐키리스 기계식 키보드");

        // when: 재고 3개 차감
        product.decreaseStock(3); //정상
        //product.decreaseStock(-5);    // 음수차감
        //product.decreaseStock(15);    // 재고부족

        // then: 남은 재고가 7개인지 검증
        assertThat(product.getStockQuantity()).isEqualTo(7);
        // fail("TODO: 테스트를 작성하세요");
    }

    @Test
    @DisplayName("재고보다 많이 차감하면 예외가 나고 재고는 그대로다")
    void 재고_부족이면_예외() {
        // given: 재고 10개인 상품 생성 - (Long sellerId, String name, BigDecimal price, Integer stockQuantity, String description)
        Product product = Product.create(153L, "키보드", BigDecimal.valueOf(125000), 10, "그루브 스톤 텐키리스 기계식 키보드");

        // when: 재고보다 많은 15개 차감
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> product.decreaseStock(15)
        );

        // then: 예외 발생 후 재고가 유지되는지 검증
        assertThat(product.getStockQuantity()).isEqualTo(10);

        // 예외 메시지 및 재고 출력
        System.out.println("예외 메시지: " + exception.getMessage());
        System.out.println("현재 재고: " + product.getStockQuantity());
        //fail("TODO: 테스트를 작성하세요");
    }

    @Test
    @DisplayName("0 이하의 수량은 차감할 수 없다")
    void 수량이_0_이하면_예외() {
        // 힌트: 지금 Product.decreaseStock(-3) 을 호출하면 재고가 늘어납니다. 고쳐 보세요.
        // given: 재고 10개인 상품 생성 - (Long sellerId, String name, BigDecimal price, Integer stockQuantity, String description)
        Product product = Product.create(153L, "키보드", BigDecimal.valueOf(125000), 10, "그루브 스톤 텐키리스 기계식 키보드");

        // when: 0개 차감 시도
        IllegalArgumentException exception1 = assertThrows(
                IllegalArgumentException.class,
                () -> product.decreaseStock(0)
        );

        // when: 음수 수량 차감 시도
        IllegalArgumentException exception2 = assertThrows(
                IllegalArgumentException.class,
                () -> product.decreaseStock(-3)
        );

        // then: 재고가 변경되지 않았는지 검증
        assertThat(product.getStockQuantity()).isEqualTo(10);

        // 예외 메시지 출력
        System.out.println("수량 0: " + exception1.getMessage());
        System.out.println("수량 -3: " + exception2.getMessage());
        System.out.println("최종 재고: " + product.getStockQuantity());

        //fail("TODO: 테스트를 작성하세요");
    }
}
