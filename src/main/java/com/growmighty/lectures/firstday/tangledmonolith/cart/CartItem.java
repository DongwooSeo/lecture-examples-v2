package com.growmighty.lectures.firstday.tangledmonolith.cart;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    public static CartItem create(Long productId, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.productId = productId;
        //cartItem.quantity = quantity;
        cartItem.changeQuantity(quantity); // 수량규칙 적용
        return cartItem;
    }

    void assignCart(Cart cart) {
        this.cart = cart;
    }

    // 수량을 변경하는 메서드가 없음.
    void changeQuantity(int quantity) {
        if(quantity < 1) {
            throw new IllegalArgumentException(
                    "상품 수량은 1 이상이어야 합니다."
            );
        }
        this.quantity = quantity;
    }
}
