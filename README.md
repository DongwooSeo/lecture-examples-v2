# 📝 과제 HW01 — 나머지 도메인 연관관계 끊기

| 항목 | 내용 |
| --- | --- |
| 마감 | **11/18 (수) 13:30** |
| 유형 | 복습 · 예상 2시간 |
| 시작 브랜치 | `homework/ddd-1` (이 브랜치) |
| 정답 예시 | `lectures/ddd/step2-done` |
| 이어지는 수업 | Day 1 · DDD ① 전략적 설계 (11/17) |

## 왜 하나요?

어제 수업에서 `상품 ↔ 주문` 경계를 **ID 간접 참조**로 끊었습니다. 같은 원칙으로 나머지 경계를 끊으면,
각 패키지가 다른 도메인을 객체로 붙잡지 않게 되고 — 나중에 서비스로 떼어낼 수 있는 상태가 됩니다.

## 할 일

**필수**

- [ ] `Cart.user`, `CartItem.product` → `userId`, `productId` (Long)
- [ ] `Seller.user`, `Seller.products` ↔ `Product.seller` 양방향 참조 제거 — 어느 쪽이 ID 를 들고 있어야 하는지 근거와 함께
- [ ] `Order.user`, `Order.payment` → `userId`, `paymentId`
- [ ] `OrderItem.product` → `productId` + **주문 당시 상품명·가격 스냅샷** (수업에서 함께 한 부분 — 본인 코드로 이어서 해도 됩니다)
- [ ] `DataInitializer`, `OrderService` 를 바뀐 구조에 맞게 고쳐 `./gradlew bootRun` 후 주문이 성공

**도전**

- [ ] 경계마다 "스냅샷이 필요한가?"를 표로 정리 (예: 주문 → 상품 가격은 필요 / 장바구니 → 상품 가격은 불필요, 왜?)

## 완료 기준 — 검증 테스트

```bash
./gradlew test --tests '*DomainBoundaryTest*'
```

`src/test/java/.../homework/DomainBoundaryTest.java` 가 모든 Entity 를 스캔해 **다른 도메인 Entity 를 JPA 연관관계로 들고 있는 필드**를 찾아냅니다.
지금 실행하면 아래처럼 끊어야 할 8곳이 실패 메시지로 나옵니다. 0개가 되면 통과입니다.

```
Cart.user → User (cart 도메인 → user 도메인)
CartItem.product → Product (cart 도메인 → product 도메인)
Order.user → User ...
```

기존 `OrderRepositoryTests` 도 함께 통과해야 합니다 (`./gradlew test`).

## 제출 방법

1. 이 레포를 **fork** 하고, 본인 fork 에서 이 브랜치를 기준으로 `hw/ddd-1` 브랜치를 만듭니다.
   ```bash
   git fetch upstream && git checkout -b hw/ddd-1 upstream/homework/ddd-1
   ```
2. **마감(11/18 (수) 13:30) 전까지** 본인 fork 의 브랜치(또는 PR) 링크를 과제 제출 채널에 올립니다.
3. 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 정답 브랜치와 나란히 비교합니다.

> 막히면 정답 브랜치 `lectures/ddd/step2-done` 와 `git diff` 로 비교해 봐도 됩니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
