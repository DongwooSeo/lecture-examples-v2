# [정산 ①-3] 멱등성과 실패 재시작

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/settlement/step3` (구현 전 · 출발 코드) |
| 강의일 | Day 4 · 11/23 (월) ~19:00 |
| 이동 | 이전: `lectures/settlement/step2-done` · 다음: `lectures/settlement/step3-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/settlement/step3
> ```

## 이 브랜치는?

Chunk 파이프라인은 완성됐지만, **중간에 실패하면?** 다시 돌리면 유니크 위반으로 터지고, 처음부터 다시 읽어야 합니다.

## 오늘 수업에서 하는 것 — Step 3

1. **3-1** 배치 실행 중 **50% 지점**에서 에러 발생 시나리오
2. **3-2** 다시 실행했을 때 이미 정산된 50%는 건너뛰고 나머지만 안전하게 처리

두 가지 다른 무기를 나란히 봅니다.

| 구분 | 무엇이 막아주나 | 재실행 시 |
| --- | --- | --- |
| **멱등성** (애플리케이션) | `existsByOrderId` 스킵 + `order_id` 유니크 | 전체를 다시 읽되 정산된 건 필터링 |
| **재시작** (프레임워크) | ExecutionContext 체크포인트, **같은** JobParameters | 마지막 커밋 지점부터 이어서 읽기 |

## 📝 과제 HW05 — 재시작 시나리오 확장 + chunk 크기 측정 (마감 11/24 (화) 13:30)

**필수**

- [ ] `SettlementRestartIntegrationTest`(`lectures/settlement/step3-done`)에 **70% 지점 실패 → 재시작** 시나리오 테스트 추가
  - 검증: 최종 정산 건수 = 대상 주문 수, `COUNT(*) = COUNT(DISTINCT order_id)`
- [ ] 시드 5만 건에서 `--settlement.batch.chunk-size=100 / 1000 / 5000`으로 `POST /settlements/batch`를 실행해 elapsed·peakHeap 표 작성
- [ ] 표를 보고 chunk 크기의 트레이드오프를 2~3줄로 정리

**도전**

- [ ] 장애 스위치(`SettlementFaultBox`)를 왜 JobParameters가 아니라 **싱글톤**에 뒀는지 설명

### 제출 방법 (공통)

1. 이 레포를 **fork** 한 뒤, 본인 fork에 `hw/settlement-1` 브랜치를 만들어 작업합니다.
2. 마감 전까지 **본인 fork의 브랜치 링크**(또는 PR 링크)를 과제 제출 채널에 올립니다.
3. 다음 날 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 `-done` 브랜치와 나란히 비교합니다.

> 막히면 `lectures/settlement/step3-done` 브랜치와 `git diff`로 비교해 봐도 괜찮습니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
