package com.growmighty.lectures.firstday.product.application.port;

/**
 * [과제 SEARCH-Outbox] Outbox 릴레이 계약 — 이 인터페이스를 구현한 빈을 하나 만드세요.
 *
 * <ul>
 *   <li>상품이 바뀌면 같은 트랜잭션 안에서 product_outbox 테이블에 "색인해야 함" 기록을 남긴다.</li>
 *   <li>릴레이는 아직 처리하지 않은 기록을 읽어 ES 에 색인하고, 성공한 것만 "처리됨"으로 표시한다.</li>
 *   <li>ES 가 죽어 있으면 예외를 밖으로 던지지 말고, 처리하지 못한 기록은 다음 실행 때 다시 시도한다.</li>
 *   <li>주기 실행은 @Scheduled 로 하되, 간격은 설정값 product.outbox.relay-delay-ms 를 쓰세요
 *       (검증 테스트가 이 값을 크게 잡아 자동 실행을 멈추고 직접 호출합니다).</li>
 * </ul>
 */
public interface OutboxRelay {

    /**
     * 미처리 Outbox 기록을 색인한다.
     *
     * @return 이번 실행에서 색인에 성공해 "처리됨"으로 표시한 건수
     */
    int relayPending();
}
