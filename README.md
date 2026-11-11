# 📝 과제 HW02 — 장바구니 애그리거트 + 불변식 단위 테스트

| 항목 | 내용 |
| --- | --- |
| 마감 | **11/19 (목) 13:30** |
| 유형 | 복습 · 예상 2.5시간 |
| 시작 브랜치 | `homework/ddd-2` (이 브랜치) |
| 정답 예시 | `homework/ddd-2-solution` |
| 이어지는 수업 | Day 2 · DDD ② 전술적 설계 (11/18) |

## 왜 하나요?

수업에서는 **주문** 애그리거트를 다듬었습니다. 장바구니·상품·결제에는 아직 "밖에서 마음대로 바꿀 수 있는" 구멍이 남아 있습니다.

| 지금 코드 | 문제 |
| --- | --- |
| `cart.addItem(CartItem.create(productId, qty))` | 밖에서 만든 항목을 그대로 넣는다 → 같은 상품이 두 줄, 수량 0 도 들어간다 |
| `product.decreaseStock(-3)` | 음수를 넣으면 재고가 **늘어난다** |
| `payment.changeStatus(아무 상태)` | PAID 된 결제를 FAILED 로 되돌릴 수 있다 |

## 할 일

**필수 — 규칙을 Entity 안으로**

- [ ] `Cart` 를 애그리거트 루트로: `cart.addItem(productId, quantity)`, `changeQuantity(productId, n)`, `removeItem(productId)` — 항목은 Cart 가 직접 만든다
  - 수량은 1 이상
  - 같은 상품을 다시 담으면 **수량 합산**
  - 없는 상품의 수량 변경 · 삭제는 예외
- [ ] `Product.decreaseStock` 에 0 이하 수량 거부 추가
- [ ] `Payment.changeStatus` 를 없애고 `approve()` / `fail()` — READY 에서만 전이 가능
- [ ] `DataInitializer`, `PaymentService` 를 새 메서드로 수정

**필수 — 규칙을 테스트로 증명**

`src/test/java/.../homework/` 의 테스트 뼈대 3개(`CartAggregateTest`, `ProductStockTest`, `PaymentStatusTest`)는 이름과 given/when/then 주석만 있고
`fail("TODO")` 로 실패합니다. `fail` 을 지우고 본문을 채우세요. **스프링 없이** 도는 순수 단위 테스트여야 합니다.

**도전**

- [ ] 한 장바구니에 담을 수 있는 상품 종류 상한(예: 50종) 규칙 추가 + 테스트
- [ ] "이 규칙은 Entity 에 둘까, Domain Service 에 둘까?" 판단 근거를 PR 본문에 3줄로

## 완료 기준

```bash
./gradlew test     # homework 테스트 12개 + 기존 테스트 모두 통과
```

테스트 이름만 읽어도 규칙이 보이면 좋은 테스트입니다 (예: `같은_상품을_담으면_수량이_합쳐진다`).

## 제출 방법

1. 이 레포를 **fork** 하고, 본인 fork 에서 이 브랜치를 기준으로 `hw/ddd-2` 브랜치를 만듭니다.
   ```bash
   git fetch upstream && git checkout -b hw/ddd-2 upstream/homework/ddd-2
   ```
2. **마감(11/19 (목) 13:30) 전까지** 본인 fork 의 브랜치(또는 PR) 링크를 과제 제출 채널에 올립니다.
3. 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 정답 브랜치와 나란히 비교합니다.

> 막히면 정답 브랜치 `homework/ddd-2-solution` 와 `git diff` 로 비교해 봐도 됩니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
