package com.growmighty.lectures.firstday.tangledmonolith.cart;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    private static final int CAPACITY = 50;

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

    public void addItem(Long productId, int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");

        if (findItem(productId) != null) {
            changeQuantity(productId, quantity);
            return;
        }

        if (items.size() == CAPACITY)
            throw new IllegalStateException("더 이상 장바구니에 담을 수 없습니다.");

        CartItem item = CartItem.create(productId, quantity);
        item.assignCart(this);
        this.items.add(item);
    }

    public void changeQuantity(Long productId, int n) {
        CartItem ex = requireItem(productId);
        if (ex.getQuantity() + n <= 0)
            throw new IllegalArgumentException("변경할 수 없는 수량입니다.");

        CartItem item = CartItem.create(productId, n + ex.getQuantity());
        this.items.remove(ex);
        this.items.add(item);
    }

    public void removeItem(Long productId) {
        CartItem cartItem = requireItem(productId);
        this.items.remove(cartItem);
    }

    public CartItem findItem(Long productId) {
        for (CartItem ci : items) {
            if (Objects.equals(ci.getProductId(), productId))
                return ci;
        }
        return null;
    }

    public CartItem requireItem(Long productId) {
        CartItem item = findItem(productId);
        if (item == null)
            throw new IllegalArgumentException("장바구니에 존재하지 않는 상품입니다.");
        return item;
    }

    public void clear() {
        this.items.clear();
    }
}
