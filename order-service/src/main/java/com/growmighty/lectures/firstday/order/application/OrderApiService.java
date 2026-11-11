package com.growmighty.lectures.firstday.order.application;

import com.growmighty.lectures.firstday.common.exception.EntityNotFoundException;
import com.growmighty.lectures.firstday.order.application.dto.OrderConsistencyView;
import com.growmighty.lectures.firstday.order.application.dto.OrderLine;
import com.growmighty.lectures.firstday.order.application.dto.OrderResult;
import com.growmighty.lectures.firstday.order.application.dto.PlaceOrderCommand;
import com.growmighty.lectures.firstday.order.application.port.PaymentPort;
import com.growmighty.lectures.firstday.order.application.port.ProductPort;
import com.growmighty.lectures.firstday.order.application.port.dto.PaymentResult;
import com.growmighty.lectures.firstday.order.application.port.dto.ProductSnapshot;
import com.growmighty.lectures.firstday.order.domain.Money;
import com.growmighty.lectures.firstday.order.domain.Order;
import com.growmighty.lectures.firstday.order.domain.OrderItem;
import com.growmighty.lectures.firstday.order.domain.OrderRepository;
import com.growmighty.lectures.firstday.order.application.exception.OrderSagaFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

/**
 * 주문 애플리케이션 서비스.
 *
 * <p>다른 도메인(product/payment)의 클래스를 직접 알지 않는다. 오직 주문이 스스로 정의한
 * {@link ProductPort} / {@link PaymentPort} 계약으로만 대화하고, 실제 통신은
 * infrastructure 의 HTTP 클라이언트가 담당한다. 이 경계 덕분에 order 는 컴파일 의존이
 * common 뿐이고, product/payment 와는 HTTP(JSON 계약)로만 연결된다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderApiService {

    private final OrderRepository orderRepository;
    private final ProductPort productPort;
    private final PaymentPort paymentPort;

    /**
     * 주문 생성 Orchestration Saga.
     * <pre>
     * T1 재고 차감(상품마다) → T2 결제 → 주문 PAID
     * 실패하면 이미 끝낸 단계만 역순으로 보상(C1 재고 복원)하고, 주문은 CANCELLED 로 저장한다.
     * </pre>
     */
    @Transactional(noRollbackFor = OrderSagaFailedException.class)
    public OrderResult placeOrder(PlaceOrderCommand command) {
        List<OrderLine> lines = command.lines();
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("주문할 상품이 없습니다.");
        }

        // 멱등: 같은 키로 이미 만든 주문이 있으면 아무것도 다시 하지 않고 그 결과를 돌려준다.
        if (command.idempotencyKey() != null) {
            Optional<Order> existing = orderRepository.findByIdempotencyKey(command.idempotencyKey());
            if (existing.isPresent()) {
                return OrderResult.from(existing.get());
            }
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderLine line : lines) {
            ProductSnapshot product = productPort.getProduct(line.productId());
            if (!product.orderable()) {
                throw new IllegalStateException("현재 구매할 수 없는 상품입니다. productId=" + product.productId());
            }
            orderItems.add(OrderItem.create(product.name(), product.price(), product.productId(), line.quantity()));
        }
        Order order = Order.create(command.userId(), orderItems, command.idempotencyKey());

        // 완료한 단계를 기록해 두었다가, 실패하면 역순으로 되돌린다.
        Deque<OrderLine> decreased = new ArrayDeque<>();
        try {
            for (OrderLine line : lines) {
                productPort.decreaseStock(line.productId(), line.quantity());   // T1
                decreased.push(line);
            }
            PaymentResult payment = paymentPort.pay(order.getTotalAmount().getValue());   // T2
            order.completePayment(payment.paymentId());
            return OrderResult.from(orderRepository.save(order));
        } catch (RuntimeException failure) {
            compensate(decreased);
            order.cancel();
            Order cancelled = orderRepository.save(order);
            throw new OrderSagaFailedException(cancelled.getId(),
                    "주문을 완료하지 못해 취소했습니다. 원인: " + failure.getMessage(), failure);
        }
    }

    /** C1: 차감했던 재고를 나중에 한 것부터 복원한다. 보상 자체가 실패하면 로그를 남기고 나머지는 계속 시도한다. */
    private void compensate(Deque<OrderLine> decreased) {
        while (!decreased.isEmpty()) {
            OrderLine line = decreased.pop();
            try {
                productPort.restoreStock(line.productId(), line.quantity());
            } catch (RuntimeException e) {
                log.error("보상 실패 — 수동 보정 필요. productId={}, quantity={}", line.productId(), line.quantity(), e);
            }
        }
    }

    @Transactional
    public OrderResult cancelOrder(Long orderId) {
        Order order = getOrder(orderId);
        order.cancel();

        for (OrderItem item : order.getItems()) {
            productPort.restoreStock(item.getProductId(), item.getQuantity());
        }
        if (order.getPaymentId() != null) {
            paymentPort.cancel(order.getPaymentId());
        }
        return OrderResult.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResult> getOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResult::from)
                .toList();
    }

    @Transactional
    public void changeItemPrice(Long orderId, Long orderItemId, BigDecimal newPrice) {
        getOrder(orderId).changeItemPrice(orderItemId, newPrice);
    }

    @Transactional
    public void changeItemQuantity(Long orderId, Long orderItemId, int newQuantity) {
        getOrder(orderId).changeItemQuantity(orderItemId, newQuantity);
    }

    @Transactional(readOnly = true)
    public OrderConsistencyView inspectOrder(Long orderId) {
        Order order = getOrder(orderId);
        Money storedTotal = order.getTotalAmount();
        Money recalculatedTotal = order.recalculatedTotal();
        return new OrderConsistencyView(
                orderId,
                storedTotal.getValue(),
                recalculatedTotal.getValue(),
                storedTotal.isSameAmount(recalculatedTotal));
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문입니다. orderId=" + orderId));
    }
}
