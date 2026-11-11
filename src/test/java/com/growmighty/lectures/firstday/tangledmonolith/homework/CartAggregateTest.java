package com.growmighty.lectures.firstday.tangledmonolith.homework;

import com.growmighty.lectures.firstday.tangledmonolith.cart.Cart;
import com.growmighty.lectures.firstday.tangledmonolith.cart.CartItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** [과제 DDD-2 정답 예시] 장바구니 애그리거트 불변식 테스트 */
class CartAggregateTest {

    @Test
    @DisplayName("새 상품을 담으면 항목이 하나 추가된다")
    void 새_상품을_담으면_항목이_추가된다() {
        Cart cart = Cart.create(1L);

        cart.addItem(10L, 2);

        assertThat(cart.getItems()).hasSize(1);
        CartItem item = cart.getItems().get(0);
        assertThat(item.getProductId()).isEqualTo(10L);
        assertThat(item.getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("같은 상품을 다시 담으면 새 항목이 아니라 수량이 합쳐진다")
    void 같은_상품을_담으면_수량이_합쳐진다() {
        Cart cart = Cart.create(1L);
        cart.addItem(10L, 2);

        cart.addItem(10L, 3);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("수량이 0 이하이면 담을 수 없다")
    void 수량이_0_이하면_예외() {
        Cart cart = Cart.create(1L);

        assertThatThrownBy(() -> cart.addItem(10L, 0)).isInstanceOf(IllegalArgumentException.class);
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    @DisplayName("담긴 상품의 수량을 바꿀 수 있다")
    void 수량을_변경한다() {
        Cart cart = Cart.create(1L);
        cart.addItem(10L, 2);

        cart.changeQuantity(10L, 7);

        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(7);
    }

    @Test
    @DisplayName("장바구니에 없는 상품의 수량을 바꾸면 예외가 난다")
    void 없는_상품의_수량_변경은_예외() {
        Cart cart = Cart.create(1L);

        assertThatThrownBy(() -> cart.changeQuantity(99L, 1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("담긴 상품을 뺄 수 있다")
    void 상품을_뺀다() {
        Cart cart = Cart.create(1L);
        cart.addItem(10L, 2);
        cart.addItem(20L, 1);

        cart.removeItem(10L);

        assertThat(cart.getItems()).extracting(CartItem::getProductId).containsExactly(20L);
    }
}
