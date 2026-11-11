# [MSA ③-2] 완성 — order-service 원격 호출을 FeignClient로

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/msa/step6-done` (구현 후 · 완성 코드) |
| 강의일 | Day 7 · 11/26 (목) |
| 이동 | 이전: `lectures/msa/step6` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/msa/step6-done
> ```

## 이 브랜치에서 달라진 것

- `order-service`: `ProductFeignClient`, `PaymentFeignClient` 추가, 기존 RestClient 설정(`OrderClientConfig`, `ProductHttpClient`) 제거
- `@EnableFeignClients` 적용 (`OrderServiceApplication`)

> `cart-service`는 아직 RestClient(`ProductHttpClient`)입니다 — **과제 8**에서 전환합니다.

## 📝 과제와의 관계

이 브랜치는 **과제 8 (cart-service Feign 전환)**의 비교 기준(정답 예시)입니다. 과제 내용은 `lectures/msa/step6` 브랜치 README를 확인하세요.

> 다음 MSA ④ (Kafka · Saga · Outbox) 브랜치는 준비 중입니다.
