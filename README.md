# [DDD ①] 연관관계 끊고 도메인 경계 나누기

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/ddd/step2` (구현 전 · 출발 코드) |
| 강의일 | Day 1 · 11/17 (화) 14:00~19:00 |
| 이동 | 이전: `main` · 다음: `lectures/ddd/step2-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/ddd/step2
> ```

## 이 브랜치는?

6개 도메인(상품·장바구니·결제·주문·유저·셀러)이 **객체 직접 참조(`@OneToOne`, `@ManyToOne` …)로 뒤엉킨** 모놀리식입니다.
주문 로직은 `OrderService` 한 곳에 트랜잭션 스크립트로 몰려 있습니다. 사전 과제에서 그린 그 코드가 출발점입니다.

## 오늘 수업에서 하는 것

| 시간 | 내용 |
| --- | --- |
| 14:00~16:00 | 강사 소개 · 예제 코드에서 "무엇이 문제일까?" 찾기 · DDD의 필요성 · 바운디드 컨텍스트 · 애그리거트와 루트 |
| 16:10~19:00 | **Step 1** draw.io로 연관관계 ERD 그리기 → **Step 2** ID 간접 참조로 연관관계 끊기 |

### Step 2 진행 방식

- **강사와 함께**: `상품 ↔ 주문` 경계 — `OrderItem.product`(객체) → `OrderItem.productId`(Long) + 주문 당시 상품명·가격 스냅샷
- **직접 실습**: 나머지 도메인 경계 (아래 과제로 이어짐)

### 지금 얽혀 있는 관계

```
Seller ──@OneToOne──▶ User
Seller ──@OneToMany─▶ Product
Product ─@ManyToOne─▶ Seller
Cart ───@OneToOne──▶ User
CartItem @OneToOne──▶ Product
Order ──@OneToOne──▶ Payment
OrderItem @ManyToOne▶ Product   ← 강사가 먼저 시연할 경계
```

## 실행 / 확인

```bash
./gradlew bootRun
curl -X POST "http://localhost:8080/orders?userId=1"   # 첫 주문 성공
./gradlew test                                         # OrderRepositoryTests
```

## 📝 오늘의 과제 (내일 오전)

| 과제 | 마감 | 시작 브랜치 | 검증 |
| --- | --- | --- | --- |
| **HW01** 나머지 도메인 연관관계 끊기 | 11/18 (수) 13:30 | `homework/ddd-1` | 브랜치 README 의 완료 기준 |

과제 내용 · 완료 기준 · 제출 방법은 **시작 브랜치의 README** 에 있습니다.
```bash
git fetch origin && git checkout homework/ddd-1
```
