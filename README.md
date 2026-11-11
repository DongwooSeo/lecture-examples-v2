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

> `cart-service`는 아직 RestClient(`ProductHttpClient`)입니다 — **과제 HW08** 에서 전환합니다.


## 📝 과제와의 관계

**과제 HW08** (`homework/msa-2`)의 정답 예시는 `homework/msa-2-solution` 브랜치에 있습니다.

## 다음 수업 — MSA ④ Kafka · Saga · Outbox (11/27)

이 브랜치 위에서 진행합니다. 수업 뒤 주말 과제는 **HW09 ⭐ 주문 생성 Saga** 입니다.

```bash
git checkout homework/msa-saga     # 멱등 키 배관 + OrderSagaTest + Kafka compose 가 준비된 시작 브랜치
```
