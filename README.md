# 📝 과제 HW09 — ⭐ 주문 생성 Saga — 보상 트랜잭션 + 멱등 키

| 항목 | 내용 |
| --- | --- |
| 마감 | **11/30 (월) 13:30** |
| 유형 | 핵심 (주말 포함) · 예상 5시간 |
| 시작 브랜치 | `homework/msa-saga` (이 브랜치) |
| 정답 예시 | `homework/msa-saga-solution` |
| 이어지는 수업 | Day 8 · MSA ④ Kafka · Saga · Outbox (11/27) |

## 왜 하나요?

지금 `OrderApiService.placeOrder()` 는 재고를 차감(product-service 에 **즉시 커밋**)한 뒤 결제를 부릅니다.
결제가 실패하면? 재고는 이미 빠졌는데 되돌릴 코드가 없습니다. 같은 요청이 재시도되면? 재고가 두 번 빠집니다.

```
 T1 재고 차감(상품마다) ──▶ T2 결제 ──▶ 주문 PAID
       │ 실패                  │ 실패
       ▼                       ▼
 C1 이미 차감한 재고만 역순 복원 ◀──┘   그리고 주문은 CANCELLED 로 "저장" (삭제 아님)
```

## 미리 준비해 둔 것

| 위치 | 내용 |
| --- | --- |
| `PlaceOrderCommand.idempotencyKey` | 멱등 키 (컨트롤러가 `Idempotency-Key` 헤더에서 꺼내 넣음) |
| `Order.idempotencyKey` (unique), `Order.create(userId, items, key)` | 같은 키로는 주문이 하나만 |
| `OrderRepository.findByIdempotencyKey` | 구현까지 완료 |
| `ProductPort.restoreStock`, `PaymentPort.cancel` | 보상에 쓸 API (이미 있음) |
| `infrastructure/kafka/docker-compose.yml` | 도전 과제용 Kafka |
| `order-service/src/test/.../homework/OrderSagaTest.java` | 검증 테스트 (지금은 4개 실패) |

## 할 일

**필수 — `OrderApiService.placeOrder` 를 Orchestration Saga 로**

- [ ] 단계마다 "완료했음"을 기록하고, 실패하면 **완료한 단계만 역순으로** 보상
- [ ] 결제 실패 → 차감했던 재고 전부 복원 → 주문은 `CANCELLED` 로 **저장** → 예외
- [ ] 두 번째 상품 재고 차감 실패 → 첫 번째 상품만 복원, 결제는 시도하지 않음
- [ ] 같은 `Idempotency-Key` 재요청 → 기존 주문 결과를 그대로 반환, 재고·결제 호출 0회
- [ ] ⚠️ `@Transactional` 메서드에서 예외를 던지면 CANCELLED 저장까지 롤백됩니다. 어떻게 막을까요? (`noRollbackFor`, 별도 예외 타입 …)

**도전 (택1)**

- [ ] 보상 자체가 실패하면? — 로그 + 재시도 → DLQ → 수동 보정, 3단 방어선 설계를 PR 본문에
- [ ] 같은 시나리오를 **Kafka Choreography** 로: `order.created` → product 가 재고 차감 후 `stock.decreased` → payment … 실패 이벤트로 보상 (spring-kafka 의존성 추가 필요)

## 완료 기준 — 검증 테스트

```bash
./gradlew :order-service:test --tests '*OrderSagaTest*'
```

| 테스트 | 검증 |
| --- | --- |
| 정상 | PAID, 보상 없음 |
| 결제 실패 | 재고 2건 복원 + 마지막 저장 상태 CANCELLED + 예외 |
| 두 번째 차감 실패 | 첫 번째만 복원, 결제 0회 |
| 역순 보상 | 나중에 차감한 상품부터 복원 |
| 멱등 | 같은 키 → 기존 주문 반환, 재고·결제 0회 |

기존 `OrderApiServiceTest` 도 계속 통과해야 합니다 (정상 경로에서 `save` 는 한 번만).

## 제출 방법

1. 이 레포를 **fork** 하고, 본인 fork 에서 이 브랜치를 기준으로 `hw/msa-saga` 브랜치를 만듭니다.
   ```bash
   git fetch upstream && git checkout -b hw/msa-saga upstream/homework/msa-saga
   ```
2. **마감(11/30 (월) 13:30) 전까지** 본인 fork 의 브랜치(또는 PR) 링크를 과제 제출 채널에 올립니다.
3. 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 정답 브랜치와 나란히 비교합니다.

> 막히면 정답 브랜치 `homework/msa-saga-solution` 와 `git diff` 로 비교해 봐도 됩니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
