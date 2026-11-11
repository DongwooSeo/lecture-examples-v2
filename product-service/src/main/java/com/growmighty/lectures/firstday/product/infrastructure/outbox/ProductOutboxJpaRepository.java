package com.growmighty.lectures.firstday.product.infrastructure.outbox;

import com.growmighty.lectures.firstday.product.domain.outbox.ProductOutbox;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOutboxJpaRepository extends JpaRepository<ProductOutbox, Long> {
    List<ProductOutbox> findByProcessedAtIsNullOrderByIdAsc(Pageable pageable);
}
