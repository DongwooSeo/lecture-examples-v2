# [DDD ②] 완성 — 도메인에 DDD 5대 요소 적용

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/ddd/step3-done` (구현 후 · 완성 코드) |
| 강의일 | Day 2 · 11/18 (수) |
| 이동 | 이전: `lectures/ddd/step3` · 다음: `lectures/ddd/step4` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/ddd/step3-done
> ```

## 이 브랜치에서 달라진 것

| 요소 | 위치 | 내용 |
| --- | --- | --- |
| VO | `order/Money.java` | 금액 연산·검증을 값 객체로 |
| Aggregate Root | `order/Order.java`, `OrderItem.java` | 항목 추가·합계·상태 전이를 `Order`가 책임 |
| Entity 행위 | `product/Product.java`, `payment/Payment.java` | setter 대신 `decreaseStock()`, 결제 상태 전이 메서드 |
| Application Service | `ProductService`, `PaymentService`, `UserService` | 도메인별 서비스로 책임 분리 |
| 스크립트 축소 | `order/OrderService.java` | 절차 코드가 대폭 줄고 도메인 메서드 호출로 |

`orders.http`에 재고 부족·결제 실패 등 확인용 시나리오가 추가되어 있습니다.

## 확인

```bash
./gradlew test
./gradlew bootRun   # orders.http 실행
```

## 📝 과제와의 관계

이 브랜치는 **과제 2 (장바구니 애그리거트 + 불변식 테스트)**의 비교 기준(정답 예시)입니다. 과제 내용은 `lectures/ddd/step3` 브랜치 README를 확인하세요.

> 장바구니 애그리거트와 단위 테스트의 예시는 `lectures/ddd/step4-done`의 `cart/domain`, `*Test.java`에 있습니다.


다음 수업은 `lectures/ddd/step4`에서 시작합니다.
