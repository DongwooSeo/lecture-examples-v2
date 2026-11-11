package com.growmighty.lectures.firstday.order.homework;

import com.growmighty.lectures.firstday.common.exception.ServiceUnavailableException;
import com.growmighty.lectures.firstday.order.application.OrderApiService;
import com.growmighty.lectures.firstday.order.application.dto.OrderLine;
import com.growmighty.lectures.firstday.order.application.dto.OrderResult;
import com.growmighty.lectures.firstday.order.application.dto.PlaceOrderCommand;
import com.growmighty.lectures.firstday.order.application.port.PaymentPort;
import com.growmighty.lectures.firstday.order.application.port.ProductPort;
import com.growmighty.lectures.firstday.order.application.port.dto.PaymentResult;
import com.growmighty.lectures.firstday.order.application.port.dto.ProductSnapshot;
import com.growmighty.lectures.firstday.order.domain.Order;
import com.growmighty.lectures.firstday.order.domain.OrderItem;
import com.growmighty.lectures.firstday.order.domain.OrderRepository;
import com.growmighty.lectures.firstday.order.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * [과제 MSA-Saga 검증 테스트] 주문 생성 Orchestration Saga
 *
 * <pre>
 *  T1 재고 차감(상품마다) ──▶ T2 결제 ──▶ 주문 PAID
 *        │ 실패                  │ 실패
 *        ▼                       ▼
 *  C1 이미 차감한 재고만 역순 복원 ◀──┘   그리고 주문은 CANCELLED 로 "저장" (삭제 아님)
 * </pre>
 *
 * OrderApiService.placeOrder 를 고쳐 이 테스트를 모두 통과시키세요.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderSagaTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductPort productPort;
    @Mock
    private PaymentPort paymentPort;

    @InjectMocks
    private OrderApiService orderApiService;

    @BeforeEach
    void setUp() {
        when(productPort.getProduct(10L)).thenReturn(new ProductSnapshot(10L, "원목 식탁", BigDecimal.valueOf(10_000), 5, true));
        when(productPort.getProduct(20L)).thenReturn(new ProductSnapshot(20L, "의자", BigDecimal.valueOf(5_000), 5, true));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.findByIdempotencyKey(any())).thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("정상: 재고 차감 → 결제 → 주문 PAID, 보상은 일어나지 않는다")
    void happyPath() {
        when(paymentPort.pay(any())).thenReturn(new PaymentResult(99L, BigDecimal.valueOf(28_000), "PAID"));

        OrderResult result = orderApiService.placeOrder(command("key-1", line(10L, 2), line(20L, 1)));

        assertThat(result.status()).isEqualTo(OrderStatus.PAID);
        verify(productPort, never()).restoreStock(anyLong(), anyInt());
    }

    @Test
    @DisplayName("결제 실패: 차감했던 재고를 모두 복원하고, 주문은 CANCELLED 로 저장한 뒤 예외를 던진다")
    void paymentFails_compensatesStockAndCancelsOrder() {
        when(paymentPort.pay(any())).thenThrow(new ServiceUnavailableException("결제 서비스 장애"));

        assertThatThrownBy(() -> orderApiService.placeOrder(command("key-2", line(10L, 2), line(20L, 1))))
                .isInstanceOf(RuntimeException.class);

        verify(productPort).restoreStock(10L, 2);
        verify(productPort).restoreStock(20L, 1);
        assertThat(lastSavedOrder().getStatus())
                .as("보상은 DELETE 가 아니라 상태 변경 — 취소된 주문이 기록으로 남아야 합니다")
                .isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("두 번째 상품의 재고 차감이 실패하면, 첫 번째 상품만 복원하고 결제는 시도하지 않는다")
    void secondStockDecreaseFails_compensatesOnlyCompletedSteps() {
        doThrow(new IllegalStateException("재고 부족")).when(productPort).decreaseStock(20L, 1);

        assertThatThrownBy(() -> orderApiService.placeOrder(command("key-3", line(10L, 2), line(20L, 1))))
                .isInstanceOf(RuntimeException.class);

        verify(productPort).restoreStock(10L, 2);
        verify(productPort, never()).restoreStock(20L, 1);
        verify(paymentPort, never()).pay(any());
    }

    @Test
    @DisplayName("보상은 역순으로 실행한다 (나중에 한 일을 먼저 되돌린다)")
    void compensationRunsInReverseOrder() {
        when(paymentPort.pay(any())).thenThrow(new ServiceUnavailableException("결제 서비스 장애"));

        assertThatThrownBy(() -> orderApiService.placeOrder(command("key-4", line(10L, 2), line(20L, 1))))
                .isInstanceOf(RuntimeException.class);

        InOrder order = inOrder(productPort);
        order.verify(productPort).restoreStock(20L, 1);
        order.verify(productPort).restoreStock(10L, 2);
    }

    @Test
    @DisplayName("멱등: 같은 Idempotency-Key 로 다시 요청하면 기존 주문을 돌려주고 재고·결제를 다시 건드리지 않는다")
    void sameIdempotencyKey_returnsExistingOrder() {
        Order existing = Order.create(1L, List.of(OrderItem.create("원목 식탁", BigDecimal.valueOf(10_000), 10L, 2)), "key-5");
        existing.completePayment(99L);
        when(orderRepository.findByIdempotencyKey("key-5")).thenReturn(Optional.of(existing));

        OrderResult result = orderApiService.placeOrder(command("key-5", line(10L, 2)));

        assertThat(result.status()).isEqualTo(OrderStatus.PAID);
        verify(productPort, never()).decreaseStock(anyLong(), anyInt());
        verify(paymentPort, never()).pay(any());
    }

    private Order lastSavedOrder() {
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, atLeastOnce()).save(captor.capture());
        return captor.getValue();
    }

    private static PlaceOrderCommand command(String key, OrderLine... lines) {
        return new PlaceOrderCommand(1L, List.of(lines), key);
    }

    private static OrderLine line(Long productId, int quantity) {
        return new OrderLine(productId, quantity);
    }
}
