package com.growmighty.lectures.firstday.product.homework;

import com.growmighty.lectures.firstday.product.application.ProductService;
import com.growmighty.lectures.firstday.product.application.dto.ProductInfo;
import com.growmighty.lectures.firstday.product.application.dto.RegisterProductCommand;
import com.growmighty.lectures.firstday.product.application.port.OutboxRelay;
import com.growmighty.lectures.firstday.product.infrastructure.search.ProductDocument;
import com.growmighty.lectures.firstday.product.infrastructure.search.ProductSearchRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

/**
 * [과제 SEARCH-Outbox 검증 테스트] ES 가 죽어 있던 동안의 변경이 "재색인 API 없이" 결국 검색에 반영되는지 확인합니다.
 *
 * 실제 ES 없이 돕니다 — ProductSearchRepository 를 가짜(Mock)로 바꿔 "ES 장애 → 복구"를 흉내 냅니다.
 */
@SpringBootTest(properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false",
        "product.outbox.relay-delay-ms=3600000"   // 자동 실행은 멈추고, 테스트가 직접 relayPending() 을 부른다
})
@ActiveProfiles("test")
class ProductOutboxRecoveryTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private OutboxRelay outboxRelay;
    @MockitoBean
    private ProductSearchRepository searchRepository;

    @Test
    @DisplayName("ES 장애 중 등록한 상품도, ES 가 살아나면 릴레이가 색인하고 다시는 중복 처리하지 않는다")
    void outboxRecoversAfterElasticsearchOutage() {
        // 1) ES 장애: 색인 시도는 모두 실패한다
        doThrow(new RuntimeException("ES down")).when(searchRepository).save(any(ProductDocument.class));
        doThrow(new RuntimeException("ES down")).when(searchRepository).saveAll(any());

        // 2) 그래도 상품 등록(본 업무)은 성공해야 한다
        ProductInfo saved = productService.register(
                new RegisterProductCommand(1L, "하객룩 원피스", BigDecimal.valueOf(89_000), 10, "결혼식 하객용"));
        assertThat(saved.id()).isNotNull();

        // 3) ES 가 아직 죽어 있으면 릴레이는 아무것도 처리하지 못하고(0건), 예외도 밖으로 새지 않는다
        assertThat(outboxRelay.relayPending()).isZero();

        // 4) ES 복구
        reset(searchRepository);
        doAnswer(invocation -> invocation.getArgument(0)).when(searchRepository).save(any(ProductDocument.class));
        doAnswer(invocation -> invocation.getArgument(0)).when(searchRepository).saveAll(any());

        // 5) 재색인 API 없이, 릴레이 한 번으로 밀린 변경이 색인된다
        assertThat(outboxRelay.relayPending()).isPositive();
        verify(searchRepository, atLeastOnce()).save(argThat(doc -> saved.id().equals(doc.getId())));

        // 6) 처리 표시가 됐으므로 다시 돌려도 처리할 것이 없다
        assertThat(outboxRelay.relayPending()).isZero();
    }
}
