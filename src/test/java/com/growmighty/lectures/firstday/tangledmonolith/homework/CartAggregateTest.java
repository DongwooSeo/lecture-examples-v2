package com.growmighty.lectures.firstday.tangledmonolith.homework;

import com.growmighty.lectures.firstday.tangledmonolith.cart.Cart;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class CartAggregateTest {

    @Test
    @DisplayName("새 상품을 담으면 항목이 하나 추가된다")
    void 새_상품을_담으면_항목이_추가된다() {
        // given: 빈 장바구니
        Cart cart = Cart.create(1L);
        // when : addItem(1L, 2)
        cart.addItem(1L, 2);
        // then : 항목 1개, 수량 2
        assertThat(cart.getItems().size() == 1);
        assertThat(cart.findItem(1L).getQuantity() == 2);
    }

    @Test
    @DisplayName("같은 상품을 다시 담으면 새 항목이 아니라 수량이 합쳐진다")
    void 같은_상품을_담으면_수량이_합쳐진다() {
        Cart cart = Cart.create(1L);
        cart.addItem(1L, 2);
        cart.addItem(1L, 3);
        assertThat(cart.getItems().size() == 1);
        assertThat(cart.findItem(1L).getQuantity() == 5);
    }

    @Test
    @DisplayName("수량이 0 이하이면 담을 수 없다")
    void 수량이_0_이하면_예외() {
        Cart cart = Cart.create(1L);
        assertThatThrownBy(() -> cart.addItem(2L, 0)).isInstanceOf(IllegalArgumentException.class);
        assertThat(cart.getItems().isEmpty());
    }

    @Test
    @DisplayName("담긴 상품의 수량을 바꿀 수 있다")
    void 수량을_변경한다() {
        Cart cart = Cart.create(1L);
        cart.addItem(1L, 1);
        cart.changeQuantity(1L, 3);
        assertThat(cart.findItem(1L).getQuantity() == 3);
    }

    @Test
    @DisplayName("장바구니에 없는 상품의 수량을 바꾸면 예외가 난다")
    void 없는_상품의_수량_변경은_예외() {
        Cart cart = Cart.create(1L);
        assertThatThrownBy(() -> cart.changeQuantity(1L, 1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("담긴 상품을 뺄 수 있다")
    void 상품을_뺀다() {
        Cart cart = Cart.create(1L);
        cart.addItem(1L, 1);
        cart.removeItem(1L);
        assertThat(cart.getItems().isEmpty());
    }

    @Test
    @DisplayName("담을 수 있는 상품의 종류에 상한이 있다")
    void 장바구니_상한() {
        Cart cart = Cart.create(1L);
        for (int i = 1; i <= 50; i++)
            cart.addItem((long) i, i);
        assertThatThrownBy(() -> cart.addItem(51L, 1)).isInstanceOf(IllegalStateException.class);
    }
}
