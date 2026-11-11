# [정산 ②] 완성 — 멀티스레드 Step과 Partitioning

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/settlement/step4-done` (구현 후 · 완성 코드) |
| 강의일 | Day 5 · 11/24 (화) |
| 이동 | 이전: `lectures/settlement/step4` · 다음: `lectures/msa/step1` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/settlement/step4-done
> ```

## 이 브랜치에서 달라진 것

| 구성 | 파일 | 설명 |
| --- | --- | --- |
| 범위 분할 | `batch/OrderRangePartitioner` | PAID 주문 id 범위를 gridSize개로 분할 |
| 워커 구성 | `batch/SettlementParallelJobConfig` | 파티션마다 `@StepScope` 전용 Reader |
| Job 조립 | `batch/SettlementParallelJobFactory` | 스레드 수 · gridSize를 런타임에 받아 Job 생성 |
| API | `POST /settlements/batch/multi-threaded?threads=N`, `.../multi-threaded/restart?runId=&failAt=`, `.../partitioned?gridSize=N` | 4-1·4-2 / 4-3 / 4-4 |
| 통합 테스트 | `SettlementPartitionIntegrationTest` | 파티셔닝 결과 무결성 검증 |

## 확인

```bash
./gradlew bootRun --args='--settlement.seed.enabled=true --settlement.seed.count=20000 \
  --settlement.batch.chunk-size=200 --spring.jpa.show-sql=false --logging.level.org.hibernate.orm.jdbc.bind=off'
```

|  | Multi-threaded Step | Partitioning |
| --- | --- | --- |
| Reader | 하나를 공유 (깔때기) | 파티션마다 전용 |
| 처리량 | 어느 선에서 천장 | 잘 확장 |
| 재시작 | `saveState=false` → 처음부터 | 파티션별 체크포인트 보존 |

상세: [`SETTLEMENT_SESSION.md`](SETTLEMENT_SESSION.md)

## 📝 과제와의 관계

이 브랜치는 **과제 6-A (측정 보고서)**의 비교 기준(정답 예시)입니다. 과제 내용은 `lectures/settlement/step4` 브랜치 README를 확인하세요.


다음 수업은 `lectures/msa/step1`에서 시작합니다.
