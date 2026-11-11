package com.growmighty.lectures.firstday.product.infrastructure.outbox;

import com.growmighty.lectures.firstday.product.application.port.OutboxRelay;
import com.growmighty.lectures.firstday.product.domain.ProductRepository;
import com.growmighty.lectures.firstday.product.domain.outbox.ProductOutbox;
import com.growmighty.lectures.firstday.product.domain.outbox.ProductOutboxRepository;
import com.growmighty.lectures.firstday.product.infrastructure.search.ProductDocument;
import com.growmighty.lectures.firstday.product.infrastructure.search.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Outbox 폴러 — 미처리 기록을 읽어 ES 에 색인하고, 성공한 것만 "처리됨"으로 표시한다.
 * ES 가 죽어 있으면 그 자리에서 멈추고(예외는 삼킨다) 다음 주기에 이어서 한다.
 * 같은 상품을 두 번 색인해도 _id = productId 라 결과는 같다(멱등 upsert).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductOutboxRelay implements OutboxRelay {

    private static final int BATCH_SIZE = 100;

    private final ProductOutboxRepository outboxRepository;
    private final ProductRepository productRepository;
    private final ProductSearchRepository searchRepository;

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${product.outbox.relay-delay-ms:1000}",
               initialDelayString = "${product.outbox.relay-delay-ms:1000}")
    public int relayPending() {
        List<ProductOutbox> pending = outboxRepository.findPending(BATCH_SIZE);
        int processed = 0;
        for (ProductOutbox outbox : pending) {
            try {
                productRepository.findById(outbox.getProductId())
                        .ifPresent(product -> searchRepository.save(ProductDocument.from(product)));
                outbox.markProcessed();
                processed++;
            } catch (RuntimeException e) {
                log.warn("Outbox 색인 실패 — 다음 주기에 재시도합니다. outboxId={}, productId={}, 원인={}",
                        outbox.getId(), outbox.getProductId(), e.toString());
                break;   // ES 장애면 뒤의 것도 실패한다. 순서를 지키며 다음 주기에 이어서.
            }
        }
        if (processed > 0) {
            log.info("Outbox 색인 {}건 처리", processed);
        }
        return processed;
    }
}
