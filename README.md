# [정산 ①-3] 완성 — 멱등성(Idempotency)과 재시작(Restartability)

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/settlement/step3-done` (구현 후 · 완성 코드) |
| 강의일 | Day 4 · 11/23 (월) |
| 이동 | 이전: `lectures/settlement/step3` · 다음: `lectures/settlement/step4` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/settlement/step3-done
> ```

## 이 브랜치에서 달라진 것

| 구성 | 파일 | 설명 |
| --- | --- | --- |
| 장애 스위치 | `batch/SettlementFaultBox` | 50% 실패를 위한 런타임 토글 |
| 멱등 Processor | `batch/OrderToSettlementProcessor` | `@StepScope` + `existsByOrderId` 스킵 + 장애 주입 |
| 실행기 | `application/SettlementBatchService` | `run()`, `runFailing(ratio)`, `runRestartable(runId, ratio)` |
| API | `POST /settlements/batch?failAt=0.5`, `POST /settlements/batch/restart?runId=1` | 3-1 / 3-2 |
| 통합 테스트 | `SettlementRestartIntegrationTest` | 50% 실패 → 재실행/재시작 → 총 건수·중복 0 검증 |

## 확인

```http
DELETE http://localhost:8080/settlements
POST   http://localhost:8080/settlements/batch?failAt=0.5     # FAILED, 약 50% 커밋
POST   http://localhost:8080/settlements/batch                # COMPLETED, skipped≈50%
POST   http://localhost:8080/settlements/batch/restart?runId=1&failAt=0.5
POST   http://localhost:8080/settlements/batch/restart?runId=1             # 이어서 COMPLETED
```

검증 SQL: `SELECT COUNT(*), COUNT(DISTINCT order_id) FROM settlements;` → 두 값이 같아야 무결

## 📝 과제와의 관계

이 브랜치는 **과제 5 (재시작 시나리오 확장)**의 비교 기준(정답 예시)입니다. 과제 내용은 `lectures/settlement/step3` 브랜치 README를 확인하세요.


다음 수업은 `lectures/settlement/step4`에서 시작합니다.
