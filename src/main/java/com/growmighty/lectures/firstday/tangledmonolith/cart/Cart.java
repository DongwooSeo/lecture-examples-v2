package com.growmighty.lectures.firstday.tangledmonolith.cart;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    // 한 바구니에 담을 수 있는 상품 종류 50개 제한
    public static final int MAX_VARIETIES = 50;

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

    /** addItem - item 추가 */
    public void addItem(Long productId, int quantity) {
        // 카트에 이미 존재하는 상품일 경우
        CartItem existing = findItem(productId); // existing으로 담아서 사용하는 이유 : 불필요한 재탐색을 피하기 위해
        if (existing != null) {
            // 추가로 담기
            existing.addQuantity(quantity);
            // 이미 존재하는 상품일 경우 상품 가짓수 추가가 문제되지 않으므로 메서드 종료.
            return;
        }

        // 카트에 존재하지 않는 상품일 경우
        // 1. 카트 내 item 종류 가짓수 확인
        if (items.size() >= MAX_VARIETIES) {
            throw new IllegalArgumentException("최대 " + MAX_VARIETIES + "종류의 상품만 담을 수 있습니다.");
        }
        // 2. item 생성
        CartItem item = CartItem.create(productId, quantity);
        // 3. cart 연결
        item.assignCart(this);
        // 4. item 추가
        items.add(item);
    }

    /** changeQuantity - 수량 변경 */
    public void changeQuantity(Long productId, int newQuantity) {
        // 카트에 존재하는 상품인지 확인
        CartItem existing = findItem(productId);
        // 존재하지 않으면 Exception
        if (existing == null) {
            throw new IllegalArgumentException("장바구니에 존재하지 않는 상품입니다.");
        } else {
            // 존재하면
            existing.changeQuantity(newQuantity);
        }
    }
    
    /** removeItem - 해당 item 지우기 */
    public void removeItem(Long productId) {
        // 카트에 존재하는 상품인지 확인
        CartItem existing = findItem(productId);
        // 존재하지 않으면 Exception
        if (existing == null) {
            throw new IllegalArgumentException("장바구니에 존재하지 않는 상품입니다.");
        } else {
            // 존재하면 지우기
            items.remove(existing);
        }
    }

    /** clear - 카트 전체 비우기 */
    public void clear() {
        this.items.clear();
    }

    /** findItem - 카트에 해당 item 이 있으면 item, 없으면 null 반환 */
    private CartItem findItem(Long productId) {
        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                return item;
            }
        }
        return null;
    }
}
