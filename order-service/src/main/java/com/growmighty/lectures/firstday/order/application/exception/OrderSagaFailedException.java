package com.growmighty.lectures.firstday.order.application.exception;

/**
 * Saga 도중 실패해 보상까지 마친 뒤 던지는 예외.
 * 이 예외로는 트랜잭션을 롤백하지 않는다 — CANCELLED 주문이 기록으로 남아야 하기 때문이다.
 */
public class OrderSagaFailedException extends RuntimeException {
    private final Long orderId;

    public OrderSagaFailedException(Long orderId, String message, Throwable cause) {
        super(message, cause);
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
