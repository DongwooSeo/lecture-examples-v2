# [DDD ③-2] 완성 — Facade vs API 호출 비교

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/ddd/step5-done` (구현 후 · 완성 코드) |
| 강의일 | Day 3 · 11/19 (목) |
| 이동 | 이전: `lectures/ddd/step5` · 다음: `lectures/settlement/step1` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/ddd/step5-done
> ```

## 이 브랜치에서 달라진 것

| 구분 | 방식 1. Facade | 방식 2. API 호출 |
| --- | --- | --- |
| 진입점 | `POST /orders/facade` | `POST /orders/api` |
| 구현 | `order/application/OrderFacade` | `order/application/OrderApiService` |
| 의존 | 상대 도메인 Service 빈 | 주문 소유 Port (`ProductPort`, `PaymentPort`) |
| 트랜잭션 | 단일 → 실패 시 전체 롤백 | 호출마다 별도 → **부분 실패 가능** |

- 어댑터: `order/infrastructure/client/ProductHttpClient`, `PaymentHttpClient` (RestClient)
- 상세 설명: [`DOMAIN_COMMUNICATION.md`](DOMAIN_COMMUNICATION.md), 실행 시나리오: `domain-communication.http`

## 확인

```bash
./gradlew bootRun
curl -X POST localhost:8080/orders/facade -H 'Content-Type: application/json' -d '{"userId":1,"requests":[{"productId":1,"quantity":1}]}'
curl -X POST localhost:8080/orders/api    -H 'Content-Type: application/json' -d '{"userId":1,"requests":[{"productId":1,"quantity":1}]}'
```

> `order.client.base-url`만 `http://product-service`로 바꾸면 그대로 MSA 호출이 됩니다. → MSA 단원에서 다시 만납니다.

## 📝 과제와의 관계

이 브랜치는 **과제 HW04 개인 파트 (트랜잭션 경계 체감)**의 비교 기준(정답 예시)입니다. 과제 내용은 `lectures/ddd/step5` 브랜치 README를 확인하세요.


다음 수업은 `lectures/settlement/step1`에서 시작합니다.
