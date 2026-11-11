package com.growmighty.lectures.firstday.product.domain.outbox;

import java.util.List;

public interface ProductOutboxRepository {
    ProductOutbox save(ProductOutbox outbox);

    /** 아직 처리하지 않은 기록을 오래된 순으로 최대 limit 건 */
    List<ProductOutbox> findPending(int limit);
}
