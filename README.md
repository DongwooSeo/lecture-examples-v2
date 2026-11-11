# 📝 과제 HW14 — ⭐ Transactional Outbox 로 검색 색인 유실 차단

| 항목 | 내용 |
| --- | --- |
| 마감 | **12/07 (월) 13:30** |
| 유형 | 핵심 (주말 포함) · 예상 5시간 |
| 시작 브랜치 | `homework/search-outbox` (이 브랜치) |
| 정답 예시 | `homework/search-outbox-solution` |
| 이어지는 수업 | Day 11 · 검색 ② Elasticsearch (12/02) |

## 왜 하나요?

지금은 상품이 바뀌면 커밋 후(`AFTER_COMMIT`) 이벤트로 ES 에 색인합니다. ES 가 죽어 있으면 예외를 삼키고 끝 — **그 변경은 유실**되고,
누군가 재색인 API 를 불러야 복구됩니다. Outbox 는 "색인해야 함"을 **상품 변경과 같은 트랜잭션**에 남겨, 결국은 반드시 색인되게 만듭니다.

```
상품 저장 ─┬─ products        (같은 트랜잭션 → 둘 다 커밋되거나 둘 다 롤백)
          └─ product_outbox  ← "productId=7 색인 필요"
                 │
   @Scheduled 릴레이 ── 미처리 기록 읽기 → ES 색인 → 처리 표시
                       ES 장애면 멈추고 다음 주기에 이어서
```

## 미리 준비해 둔 것

- `product-service/.../application/port/OutboxRelay.java` — 릴레이 계약 (`int relayPending()`)
- `product-service/src/test/.../homework/ProductOutboxRecoveryTest.java` — ES 를 Mock 으로 바꿔 장애 → 복구를 흉내 내는 검증 테스트

## 할 일

**필수**

- [ ] `product_outbox` 테이블 (Entity): productId, 생성 시각, 처리 시각(null = 미처리)
- [ ] `ProductService` 의 변경 메서드(등록 · 가격 변경 · 재고 차감/복원)가 **같은 트랜잭션에서** Outbox 기록 저장
- [ ] `OutboxRelay` 구현 빈 — 미처리 기록을 오래된 순으로 읽어 색인, 성공한 것만 처리 표시, ES 장애 예외는 밖으로 던지지 않음
- [ ] `@Scheduled(fixedDelayString = "${product.outbox.relay-delay-ms:1000}")` 로 주기 실행 (`@EnableScheduling`)
- [ ] 실제 ES 로 장애 실험: `docker stop lecture-es` → 상품 등록 → `docker start lecture-es` → **재색인 API 없이** 검색에 나타나는지 확인 (영상 또는 캡처)

**도전 (택1)**

- [ ] [정합성] 상품 삭제/판매종료 시 색인 처리 — delete vs filter 비교
- [ ] [운영] 처리된 Outbox 기록 정리(보관 기간) 배치
- [ ] [운영] alias 기반 무중단 재색인 (`products_v2` → `_aliases` 원자적 스위치)

## 완료 기준 — 검증 테스트

```bash
./gradlew :product-service:test --tests '*ProductOutboxRecoveryTest*'
```

ES 장애 중 등록 → 등록 성공 → 릴레이 0건(예외 없음) → ES 복구 → 릴레이가 해당 상품 색인 → 다시 돌리면 0건(중복 처리 없음).

## 제출 방법

1. 이 레포를 **fork** 하고, 본인 fork 에서 이 브랜치를 기준으로 `hw/search-outbox` 브랜치를 만듭니다.
   ```bash
   git fetch upstream && git checkout -b hw/search-outbox upstream/homework/search-outbox
   ```
2. **마감(12/07 (월) 13:30) 전까지** 본인 fork 의 브랜치(또는 PR) 링크를 과제 제출 채널에 올립니다.
3. 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 정답 브랜치와 나란히 비교합니다.

> 막히면 정답 브랜치 `homework/search-outbox-solution` 와 `git diff` 로 비교해 봐도 됩니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
