package com.growmighty.lectures.firstday.tangledmonolith.homework;

import com.growmighty.lectures.firstday.tangledmonolith.cart.Cart;
import com.growmighty.lectures.firstday.tangledmonolith.cart.CartItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * [과제 DDD-2] 장바구니 애그리거트 불변식 테스트 — 뼈대입니다.
 *
 * 1. 먼저 Cart / CartItem 을 아래 규칙대로 고치세요.
 * 2. 각 테스트의 fail(...) 줄을 지우고 given / when / then 을 채우세요.
 * 3. 스프링 없이 도는 순수 단위 테스트로 작성합니다 (@SpringBootTest 금지).
 *
 * 목표 API 예시 (이름은 바꿔도 됩니다)
 *   Cart cart = Cart.create(userId);
 *   cart.addItem(productId, quantity);
 *   cart.changeQuantity(productId, newQuantity);
 *   cart.removeItem(productId);
 */
class CartAggregateTest {

    @Test
    @DisplayName("새 상품을 담으면 항목이 하나 추가된다")
    void 새_상품을_담으면_항목이_추가된다() {
        // given: 빈 장바구니
        Cart cart = Cart.create(1L);
        // when : addItem(1L, 2)
        cart.addItem(1L,2);
        // then : 항목 1개, 수량 2
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());

        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(2);

        System.out.println(assertThat(cart.getItems().get(0).getQuantity()));
        System.out.println("상품 종류 수: " + cart.getItems().size());
        System.out.println("수량: " + cart.getItems().get(0).getQuantity());

       // fail("TODO: 테스트를 작성하세요");
    }

    @Test
    @DisplayName("같은 상품을 다시 담으면 새 항목이 아니라 수량이 합쳐진다")
    void 같은_상품을_담으면_수량이_합쳐진다() {
        // given: 빈 장바구니
        Cart cart = Cart.create(1L);

        // when: 동일한 상품을 두 번 추가
        cart.addItem(100L, 2);
        cart.addItem(100L, 3);

        // then: 동일 상품이므로 항목은 1개만 존재
        assertEquals(1, cart.getItems().size());

        // then: 기존 수량 2 + 추가 수량 3 = 5
        assertEquals(5, cart.getItems().get(0).getQuantity());
        //fail("TODO: 테스트를 작성하세요");
        for (CartItem item : cart.getItems()) {
            System.out.println("상품 ID: " + item.getProductId());
            System.out.println("수량: " + item.getQuantity());
        }


    }

    @Test
    @DisplayName("수량이 0 이하이면 담을 수 없다")
    void 수량이_0_이하면_예외() {
        // given: 빈 장바구니 생성
        Cart cart = Cart.create(1L);

        // when & then: 수량이 0이면 예외 발생
        IllegalArgumentException exception1 = assertThrows(
                IllegalArgumentException.class,
                () -> cart.addItem(100L, 0)
        );

        // when & then: 수량이 음수이면 예외 발생
        IllegalArgumentException exception2 = assertThrows(
                IllegalArgumentException.class,
                () -> cart.addItem(100L, -3)
        );

        // 예외 메시지 출력
        System.out.println("수량 0: " + exception1.getMessage());
        System.out.println("수량 -3: " + exception2.getMessage());

        // then: 잘못된 상품은 장바구니에 추가되지 않아야 한다
        assertEquals(0, cart.getItems().size());
        //fail("TODO: 테스트를 작성하세요");
    }

    @Test
    @DisplayName("담긴 상품의 수량을 바꿀 수 있다")
    void 수량을_변경한다() {
        // given: 빈 장바구니 생성 후, 상품추가
        Cart cart = Cart.create(1L);
        cart.addItem(100L, 2);

        // 변경 전 수량
        System.out.println("변경 전 수량: " + cart.getItems().get(0).getQuantity());

        // when : 상품 100번의 수량을 5개로 변경
        cart.changeQuantity(100L, 5);

        // 변경 후 수량
        System.out.println("변경 후 수량: " + cart.getItems().get(0).getQuantity());

        // then : 수량이 5개로 변경되었는지 확인
        assertEquals(5, cart.getItems().get(0).getQuantity());

        //fail("TODO: 테스트를 작성하세요");
    }

    @Test
    @DisplayName("장바구니에 없는 상품의 수량을 바꾸면 예외가 난다")
    void 없는_상품의_수량_변경은_예외() {
        // given: 장바구니 생성 및 상품 추가
        Cart cart = Cart.create(1L);
        cart.addItem(100L, 2);

        // when: 없는 상품 999번의 수량 변경
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cart.changeQuantity(999L, 5)
        );

        // then: 예외 메시지 검증 및 출력
        assertEquals("장바구니에 없는 상품입니다.", exception.getMessage());
        System.out.println("예외 메시지: " + exception.getMessage());
        //fail("TODO: 테스트를 작성하세요");
    }

    @Test
    @DisplayName("담긴 상품을 뺄 수 있다")
    void 상품을_뺀다() {

        // given: 장바구니 생성 및 상품 2개 추가
        Cart cart = Cart.create(1L);
        cart.addItem(100L, 2);
        cart.addItem(200L, 3);

        // when: 상품 100번 삭제
        cart.removeItem(100L);

        // then: 삭제 후 상품 200번만 남아 있는지 검증
        assertThat(cart.getItems())
                .hasSize(1)
                .extracting(CartItem::getProductId)
                .containsExactly(200L);

        // 삭제 결과 출력
        System.out.println("삭제 후 상품 목록: " +
                cart.getItems().stream()
                        .map(item -> item.getProductId() + "번 상품 / " + item.getQuantity() + "개")
                        .toList());
        //fail("TODO: 테스트를 작성하세요");
    }

    @Test
    @DisplayName("장바구니에는 최대 50종의 상품만 담을 수 있다")
    void 상품_종류는_50종을_초과할_수_없다() {
        // given: 장바구니에 서로 다른 상품 50종 추가
        Cart cart = Cart.create(1L);

        for (long productId = 1; productId <= 50; productId++) {
            cart.addItem(productId, 1);
        }

        // when: 51번째 새로운 상품 추가 시도
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> cart.addItem(51L, 1)
        );

        // then: 상품 종류는 50종으로 유지
        assertThat(cart.getItems()).hasSize(50);

        System.out.println("예외 메시지: " + exception.getMessage());
        System.out.println("현재 상품 종류: " + cart.getItems().size());

        // fail("TODO: 테스트를 작성하세요");
    }
}
