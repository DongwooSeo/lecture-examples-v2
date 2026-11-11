# [DDD ②] 전술적 설계 — DDD 5대 요소 적용하기

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/ddd/step3` (구현 전 · 출발 코드) |
| 강의일 | Day 2 · 11/18 (수) 14:00~19:00 |
| 이동 | 이전: `lectures/ddd/step2-done` · 다음: `lectures/ddd/step3-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/ddd/step3
> ```

## 이 브랜치는?

연관관계는 끊었지만, 비즈니스 규칙은 여전히 `OrderService.placeOrder()`에 **절차적으로** 몰려 있습니다.
재고 차감·합계 계산·결제·상태 전이를 서비스가 setter로 밀어 넣는 **트랜잭션 스크립트** 상태입니다.
(Step 2 완성 코드에 정합성 확인용 `OrderConsistencyView`, `OrderItemRepository`가 추가돼 있습니다.)

## 오늘 수업에서 하는 것

| 시간 | 내용 |
| --- | --- |
| 14:00~15:30 | DDD 5대 요소 (Entity · VO · Repository · Aggregate · Domain Service), 도메인 모델 vs 트랜잭션 스크립트 |
| 15:40~19:00 | **Step 3** 라이브 코딩 |

### Step 3 순서

1. **3-1** 현재 `OrderService`의 문제 인식 — 어디서 불변식이 깨질 수 있나?
2. **3-2** `Money` VO 도입 (금액 연산·검증을 값 객체로)
3. **3-3** `Order ↔ OrderItem` 애그리거트 루트 — `OrderItem`은 `Order`를 통해서만 변경
4. **3-4** Domain Service — 한 Entity에 속하지 않는 규칙의 자리
5. **3-5** 파편화된 로직을 Entity 안으로: `Product.decreaseStock()`, `Payment.pay()`, 합계 계산

## 실행 / 확인

```bash
./gradlew bootRun
# orders.http 의 시나리오로 주문 / 재고 부족 / 결제 흐름 확인
./gradlew test
```

## 📝 오늘의 과제 (내일 오전)

| 과제 | 마감 | 시작 브랜치 | 검증 |
| --- | --- | --- | --- |
| **HW02** 장바구니 애그리거트 + 불변식 단위 테스트 | 11/19 (목) 13:30 | `homework/ddd-2` | 브랜치 README 의 완료 기준 |

과제 내용 · 완료 기준 · 제출 방법은 **시작 브랜치의 README** 에 있습니다.
```bash
git fetch origin && git checkout homework/ddd-2
```
