package com.growmighty.lectures.firstday.tangledmonolith.cart;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(nullable = false)
    private Long userId;

    public static Cart create(Long userId) {
        Cart cart = new Cart();
        cart.userId = userId;
        return cart;
    }

    /*
        전달받은 CartItem을 그대로 장바구니에 추가
        CartItem create(Long productId, int quantity)
        -> 문제 1. 수량이 0이어도 추가됨
        -> 문제 2. 같은 상품이 중복 추가됨.

    public void addItem(CartItem item) {
        this.items.add(item);

        if (item.getCart() != this) {
            item.assignCart(this);
        }
    }
     */

    private static final int MAX_ITEM_TYPES = 50;

    public void addItem(Long productId, int quantity) {
        // 상품 수량 검증 - 1개 이상만 허용
        if (quantity < 1) {
            throw new IllegalArgumentException(
                    "상품 수량은 1 이상이어야 합니다."
            );
        }

        // Cart에 동일한 상품이 이미 존재하는지 조회
        CartItem target = items.stream()
                .filter(e -> e.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        // 동일한 상품이 존재하면 기존 수량에 추가
        if (target != null) {
            target.changeQuantity(Math.addExact(target.getQuantity(), quantity));
            return;
        }

        // 한 장바구니에 담을 수 있는 상품 종류 상한(예: 50종) 규칙
        if(items.size() >= MAX_ITEM_TYPES) {
            throw new IllegalStateException("장바구니에는 최대 50종의 상품만 담을 수 있습니다.");
        }

        // 새로운 상품 생성 및 추가
        CartItem newItem = CartItem.create(productId, quantity);
        this.items.add(newItem);
        newItem.assignCart(this);
    }

    public void clear() {
        this.items.clear();
    }

    public void changeQuantity(Long productId, int newQuantity) {

        //장바구니에서 상품 ID가 일치하는 항목 조회
        CartItem target = items.stream()
                .filter(e -> e.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("장바구니에 없는 상품입니다.")
                );

        //해당 상품의 수량 변경 (수량 검증은 CartItem에서 처리)
        target.changeQuantity(newQuantity);
    }

    public void removeItem(Long productId) {

        //장바구니에서 상품 ID가 일치하는 항목 조회
        CartItem target = items.stream()
                .filter(e -> e.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("장바구니에 없는 상품입니다.")
                );

        //장바구니 목록에서 해당 항목 제거
        this.items.remove(target);

        //양방향 연관관계 해제 (CartItem의 cart 참조 제거)
        target.assignCart(null);
    }
}
