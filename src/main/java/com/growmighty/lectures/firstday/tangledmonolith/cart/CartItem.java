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

    // new CartItem() cartItem -> this 변경 이유 : 생성된 인스턴스에 적용하는 것이기 때문.
    private CartItem (Long productId, int quantity) {
        checkQuantity(quantity);
        this.productId = productId;
        this.quantity = quantity;
    }

    /** create - 아이템 생성 */
    public static CartItem create(Long productId, int quantity) {
        return new CartItem(productId, quantity);
    }

    /** addQuantity - 수량 추가 */
    void addQuantity(int quantity) {
        checkQuantity(quantity);
        this.quantity += quantity;
    }

    /** changeQuantity - 수량 변경 */
    void changeQuantity(int newQuantity) {
        checkQuantity(newQuantity);
        this.quantity = newQuantity;
    }

    /** assignCart - 카트 지정 */
    void assignCart(Cart cart) {
        this.cart = cart;
    }

    /** checkQuantity - 수량 확인 */
    private void checkQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("1개 이상의 상품을 담아주세요.");
        }
    }
}
