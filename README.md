# ✅ 과제 HW02 정답 예시

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `homework/ddd-2-solution` |
| 과제 | `homework/ddd-2` 브랜치 README |

> 정답은 하나가 아닙니다. 이 브랜치는 **검증 테스트를 통과하는 한 가지 방법**입니다.
> 본인 코드와 비교할 때는 `git diff homework/ddd-2 homework/ddd-2-solution` 로 무엇이 바뀌었는지부터 보세요.

## 바뀐 것

- `cart/Cart.java`, `CartItem.java` — `addItem(productId, quantity)` 로 항목 생성·수량 합산, 수량 1~99, 없는 항목 변경 시 예외
- `product/Product.java` — `decreaseStock` 0 이하 수량 거부
- `payment/Payment.java` — `changeStatus` 제거, `approve()` / `fail()` (READY 에서만)
- `homework/*Test.java` — 테스트 뼈대를 채운 예시 12개

## 확인

과제 브랜치의 완료 기준 명령을 그대로 실행하면 모두 통과합니다.
