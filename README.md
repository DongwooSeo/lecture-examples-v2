# ✅ 과제 HW14 정답 예시

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `homework/search-outbox-solution` |
| 과제 | `homework/search-outbox` 브랜치 README |

> 정답은 하나가 아닙니다. 이 브랜치는 **검증 테스트를 통과하는 한 가지 방법**입니다.
> 본인 코드와 비교할 때는 `git diff homework/search-outbox homework/search-outbox-solution` 로 무엇이 바뀌었는지부터 보세요.

## 바뀐 것

- `domain/outbox/ProductOutbox` + `ProductOutboxRepository` (+ JPA 어댑터)
- `ProductService` — 이벤트 발행 대신 같은 트랜잭션에 Outbox 기록 (`ProductIndexer`, `ProductChangedEvent` 제거)
- `infrastructure/outbox/ProductOutboxRelay` — 미처리 100건씩, 성공분만 처리 표시, ES 장애 시 중단 후 다음 주기에 재시도
- `OutboxSchedulingConfig` — `@EnableScheduling`

## 확인

과제 브랜치의 완료 기준 명령을 그대로 실행하면 모두 통과합니다.
