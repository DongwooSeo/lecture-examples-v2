# [DDD ①] 완성 — 연관관계 끊기 + 도메인 경계 설정

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/ddd/step2-done` (구현 후 · 완성 코드) |
| 강의일 | Day 1 · 11/17 (화) |
| 이동 | 이전: `lectures/ddd/step2` · 다음: `lectures/ddd/step3` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/ddd/step2-done
> ```

## 이 브랜치에서 달라진 것

- 도메인 경계를 넘는 객체 참조를 모두 **ID 간접 참조**로 교체 (`Cart`, `CartItem`, `Order`, `OrderItem`, `Product`, `Seller`)
- `OrderItem`은 주문 당시 상품 정보를 직접 보관 → 상품 가격이 바뀌어도 주문 내역은 그대로
- `OrderService`, `DataInitializer`, `OrderRepositoryTests`를 바뀐 구조에 맞게 수정

## 확인

```bash
./gradlew test
./gradlew bootRun && curl -X POST "http://localhost:8080/orders?userId=1"
```

## 생각해 볼 것

- 객체 참조를 끊으니 **무엇을 잃었나요?** (지연 로딩으로 쉽게 따라가던 탐색, JPA cascade …)
- 대신 **무엇을 얻었나요?** (변경 영향 범위 축소, 나중에 서비스로 분리할 수 있는 경계)

## 📝 과제와의 관계

이 브랜치는 **과제 1 (나머지 도메인 연관관계 끊기)**의 비교 기준(정답 예시)입니다. 과제 내용은 `lectures/ddd/step2` 브랜치 README를 확인하세요.


다음 수업은 `lectures/ddd/step3`에서 시작합니다.
