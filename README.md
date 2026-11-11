# [MSA ③-1] 완성 — Resilience4j 서킷 브레이커

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/msa/step5-done` (구현 후 · 완성 코드) |
| 강의일 | Day 7 · 11/26 (목) |
| 이동 | 이전: `lectures/msa/step5` · 다음: `lectures/msa/step6` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/msa/step5-done
> ```

## 이 브랜치에서 달라진 것

| 서비스 | 파일 | 내용 |
| --- | --- | --- |
| order-service | `infrastructure/client/OrderCircuitBreakerConfig`, `PaymentHttpClient` | 결제 호출에 서킷 브레이커 + Fallback |
| cart-service | `infrastructure/client/CartCircuitBreakerConfig`, `ProductHttpClient` | 상품 조회에 서킷 브레이커 + Fallback |
| payment-service | `infrastructure/FakePaymentGateway` | 지연·실패를 흉내 내는 가짜 PG |
| common | `ServiceUnavailableException`, `ErrorCode` | 503 공통 응답 |

다음 단계는 `lectures/msa/step6`입니다.
