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

## 📝 과제 1 — 나머지 도메인 연관관계 끊기 (마감 11/18 (수) 13:30)

> 수업에서 `상품 ↔ 주문`을 함께 끊었습니다. 같은 원칙으로 **나머지 경계**를 끊어 오세요.

**필수**

- [ ] `Cart → User`, `CartItem → Product` 를 ID 참조로 변경
- [ ] `Seller ↔ User`, `Seller ↔ Product` 양방향 참조 제거 (어느 쪽이 ID를 들고 있어야 하는지 근거와 함께)
- [ ] `Order → Payment` 를 ID 참조로 변경
- [ ] `DataInitializer`, `OrderService` 를 바뀐 구조에 맞게 수정해 `bootRun` + 주문 호출이 성공
- [ ] `./gradlew test` 통과

**도전**

- [ ] 각 경계를 끊으며 "주문 당시 정보 보존(스냅샷)"이 필요한 곳과 필요 없는 곳을 표로 정리

**완료 기준**: 다른 도메인 Entity를 import하는 Entity가 **0개** (`grep -r "import .*\.product\.Product" src/main` 등으로 확인)

### 제출 방법 (공통)

1. 이 레포를 **fork** 한 뒤, 본인 fork에 `hw/ddd-1` 브랜치를 만들어 작업합니다.
2. 마감 전까지 **본인 fork의 브랜치 링크**(또는 PR 링크)를 과제 제출 채널에 올립니다.
3. 다음 날 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 `-done` 브랜치와 나란히 비교합니다.

> 막히면 `lectures/ddd/step2-done` 브랜치와 `git diff`로 비교해 봐도 괜찮습니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
