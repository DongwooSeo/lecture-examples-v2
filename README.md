# ✅ 과제 HW09 정답 예시

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `homework/msa-saga-solution` |
| 과제 | `homework/msa-saga` 브랜치 README |

> 정답은 하나가 아닙니다. 이 브랜치는 **검증 테스트를 통과하는 한 가지 방법**입니다.
> 본인 코드와 비교할 때는 `git diff homework/msa-saga homework/msa-saga-solution` 로 무엇이 바뀌었는지부터 보세요.

## 바뀐 것

- `OrderApiService.placeOrder` — 단계별 완료 기록(Deque) → 실패 시 역순 보상 → `CANCELLED` 저장 → `OrderSagaFailedException`
- `@Transactional(noRollbackFor = OrderSagaFailedException.class)` — 예외를 던져도 취소 기록은 남긴다
- 멱등: `findByIdempotencyKey` 로 기존 주문이 있으면 그대로 반환
- 보상 실패는 로그로 남기고 나머지 보상은 계속 (수동 보정 대상)

## 확인

과제 브랜치의 완료 기준 명령을 그대로 실행하면 모두 통과합니다.
