package com.growmighty.lectures.firstday.product.infrastructure.outbox;

import com.growmighty.lectures.firstday.product.domain.outbox.ProductOutbox;
import com.growmighty.lectures.firstday.product.domain.outbox.ProductOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductOutboxRepositoryAdapter implements ProductOutboxRepository {
    private final ProductOutboxJpaRepository jpaRepository;

    @Override
    public ProductOutbox save(ProductOutbox outbox) {
        return jpaRepository.save(outbox);
    }

    @Override
    public List<ProductOutbox> findPending(int limit) {
        return jpaRepository.findByProcessedAtIsNullOrderByIdAsc(PageRequest.of(0, limit));
    }
}
