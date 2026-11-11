# [DDD ③-1] 4계층 구조 + DIP 적용하기

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/ddd/step4` (구현 전 · 출발 코드) |
| 강의일 | Day 3 · 11/19 (목) 14:00~16:30 |
| 이동 | 이전: `lectures/ddd/step3-done` · 다음: `lectures/ddd/step4-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/ddd/step4
> ```

## 이 브랜치는?

도메인 모델은 갖췄지만, 한 패키지 안에 controller · service · entity · repository가 **섞여** 있습니다.
도메인이 JPA·웹 같은 기술에 직접 의존하고 있어, 나중에 서비스로 떼어내기 어렵습니다.

## 오늘 수업에서 하는 것

- 개념: DDD 4계층 (presentation / application / domain / infrastructure) 과 **DIP**
  - 도메인이 `Repository` **인터페이스**를 소유하고, JPA 구현은 infrastructure에 둔다
- **Step 4** 라이브 코딩: **주문 도메인**을 강사와 함께 4계층으로 분리 → 나머지는 실습/과제

```
order/
├── presentation/   ← Controller, 요청·응답 DTO
├── application/    ← 유스케이스 (OrderService)
├── domain/         ← Order, OrderItem, Money, OrderRepository(인터페이스)
└── infrastructure/ ← JPA 구현 (OrderRepositoryAdapter 등)
```

## 📝 오늘의 과제 (내일 오전)

| 과제 | 마감 | 시작 브랜치 | 검증 |
| --- | --- | --- | --- |
| **HW03** 6개 도메인 4계층 + DIP + 공통 응답 | 11/20 (금) 13:30 | `homework/ddd-3` | 브랜치 README 의 완료 기준 |

과제 내용 · 완료 기준 · 제출 방법은 **시작 브랜치의 README** 에 있습니다.
```bash
git fetch origin && git checkout homework/ddd-3
```
