package com.growmighty.lectures.firstday.tangledmonolith;

import com.growmighty.lectures.firstday.tangledmonolith.order.Order;
import com.growmighty.lectures.firstday.tangledmonolith.order.OrderItem;
import com.growmighty.lectures.firstday.tangledmonolith.order.OrderRepository;
import com.growmighty.lectures.firstday.tangledmonolith.product.Product;
import com.growmighty.lectures.firstday.tangledmonolith.seller.Seller;
import com.growmighty.lectures.firstday.tangledmonolith.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("주문 저장 및 조회 테스트")
    void saveAndFindOrderTest() {
        User user = User.register("dannyseo@growmighty", "aaa33242", "Danny", "010-0000-0000");
        Seller seller = Seller.create(user.getId());
        Product product = Product.create(seller.getId(), "Dofia 이동식 접이식 식탁 의자 4개 세트 가정용 소형주택 신축식", BigDecimal.valueOf(179000), 10, "test");
        entityManager.persist(user);
        entityManager.persist(seller);
        entityManager.persist(product);

        List<OrderItem> items = new ArrayList<>();
        items.add(OrderItem.create(product, 1));
        Order order = Order.create(user.getId(), items);

        Order saved = orderRepository.save(order);
        entityManager.flush();
        entityManager.clear();

        Order found = orderRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getItems()).hasSize(1);
        OrderItem foundItem = found.getItems().get(0);
        assertThat(foundItem.getQuantity()).isEqualTo(1);
        //테스트 코드를 보니 id, name, price 있길래 여기서 착안하여 기존 스냅샷 코드 수정
        //테스트 코드에서도 필드값 호출 시, 의존성이 높아 다음과 같이 축약 가능
        assertThat(foundItem.getProductId()).isEqualTo(product.getId());
        assertThat(foundItem.getProductName()).isEqualTo(product.getName());
        assertThat(foundItem.getUnitPrice()).isEqualByComparingTo(product.getPrice());
    }
}
