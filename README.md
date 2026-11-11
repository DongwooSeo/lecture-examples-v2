# [MSA ③-1] 서킷 브레이커 — 도미노는 여기서 끊습니다

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/msa/step5` (구현 전 · 출발 코드) |
| 강의일 | Day 7 · 11/26 (목) 14:00~16:30 |
| 이동 | 이전: `lectures/msa/step4-done` · 다음: `lectures/msa/step5-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/msa/step5
> ```

## 이 브랜치는?

설정까지 중앙화된 MSA입니다. 하지만 **payment-service 하나가 느려지거나 죽으면**, 이를 호출하는 order-service도 스레드를 붙잡힌 채 같이 무너집니다.

## Step 5에서 할 것

- [ ] payment-service를 강제로 죽이거나 지연시켜 **장애 전파** 직접 보기
- [ ] Resilience4j CircuitBreaker 적용 → 차단(OPEN) 상태에서 빠르게 실패
- [ ] Fallback으로 우아하게 실패하기 (`ServiceUnavailableException` → 503 공통 응답)
- [ ] 개념: Retry · CircuitBreaker · TimeLimiter, 재시도할 에러(503·408·429) vs 하면 안 되는 에러(400·404·422)
