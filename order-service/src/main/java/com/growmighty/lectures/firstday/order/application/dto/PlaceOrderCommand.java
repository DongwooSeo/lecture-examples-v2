package com.growmighty.lectures.firstday.order.application.dto;

import java.util.List;

/**
 * @param idempotencyKey 같은 주문 요청의 재시도를 구분하는 키 (클라이언트가 Idempotency-Key 헤더로 보냄, 없으면 null)
 */
public record PlaceOrderCommand(Long userId, List<OrderLine> lines, String idempotencyKey) {

    public PlaceOrderCommand(Long userId, List<OrderLine> lines) {
        this(userId, lines, null);
    }
}
