# [정산 ①-2] 완성 — Chunk 지향 정산 파이프라인

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/settlement/step2-done` (구현 후 · 완성 코드) |
| 강의일 | Day 4 · 11/23 (월) |
| 이동 | 이전: `lectures/settlement/step2` · 다음: `lectures/settlement/step3` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/settlement/step2-done
> ```

## 이 브랜치에서 달라진 것

| 구성 | 파일 | 설명 |
| --- | --- | --- |
| ItemReader | `batch/SettlementJobConfig#settlementOrderReader` | `JpaPagingItemReader<Order>` — PAID 주문을 id 순 페이지로 |
| ItemProcessor | `batch/OrderToSettlementProcessor` | `Order → Settlement` 변환, 1원 무결성은 `Settlement.of`에 위임 |
| ItemWriter | `batch/SettlementJobConfig#settlementWriter` | `JpaItemWriter<Settlement>` chunk 단위 적재 |
| 실행기 | `application/SettlementBatchService` | `JobOperator.start(job, params)` + HeapMonitor |
| API | `POST /settlements/batch` | read / written / elapsed / peakHeap 리포트 |

## 확인

```bash
./gradlew bootRun --args='--settlement.seed.enabled=true --settlement.seed.count=50000 --spring.jpa.show-sql=false'
curl -X DELETE http://localhost:8080/settlements
curl -X POST   http://localhost:8080/settlements/batch
```

> 이 시점에는 같은 데이터로 배치를 **두 번** 돌리면 `order_id` 유니크 위반으로 FAILED가 납니다. → 다음 Step 3의 출발점.

다음 수업은 `lectures/settlement/step3`에서 이어집니다.
