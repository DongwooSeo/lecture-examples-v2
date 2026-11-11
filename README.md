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

## 📝 과제 2 — 장바구니 애그리거트 + 불변식 테스트 (마감 11/19 (목) 13:30)

> 수업에서는 **주문** 애그리거트를 완성했습니다. 이번엔 손대지 않은 **장바구니**를 같은 방식으로 다듬고, 규칙을 **테스트로 증명**합니다.

**필수**

- [ ] `Cart`를 애그리거트 루트로: 지금처럼 밖에서 만든 `CartItem`을 넣는 대신 `cart.addItem(productId, quantity)`처럼 **Cart가 직접** 항목을 만들고 수량 변경·삭제도 `Cart`의 메서드로만
- [ ] 불변식 3가지를 Entity 안에서 강제
  - 수량은 1 이상
  - 같은 상품을 다시 담으면 새 항목이 아니라 **수량 합산**
  - 존재하지 않는 항목의 수량 변경은 예외
- [ ] 스프링 없이 도는 **순수 단위 테스트** 작성: `CartTest`, `ProductTest`(재고 차감·부족), `PaymentTest`(상태 전이)

**도전**

- [ ] `MoneyTest` — 음수 금액, 더하기/곱하기, 동등성(equals) 검증
- [ ] "이 규칙은 Entity에 둘까, Domain Service에 둘까?" 판단 근거를 README나 PR 본문에 3줄로

**완료 기준**: `./gradlew test` 통과 + 테스트 이름만 읽어도 규칙이 보일 것 (예: `같은_상품을_담으면_수량이_합쳐진다`)

> 비교 자료: `lectures/ddd/step3-done`(주문 애그리거트), `lectures/ddd/step4-done`의 `CartTest`·`ProductTest`·`PaymentTest`·`MoneyTest`

### 제출 방법 (공통)

1. 이 레포를 **fork** 한 뒤, 본인 fork에 `hw/ddd-2` 브랜치를 만들어 작업합니다.
2. 마감 전까지 **본인 fork의 브랜치 링크**(또는 PR 링크)를 과제 제출 채널에 올립니다.
3. 다음 날 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 `-done` 브랜치와 나란히 비교합니다.

> 막히면 `lectures/ddd/step3-done` 브랜치와 `git diff`로 비교해 봐도 괜찮습니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
