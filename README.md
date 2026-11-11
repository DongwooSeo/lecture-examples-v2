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

## 📝 과제 3 — 나머지 도메인 4계층 + 공통 응답 (마감 11/20 (금) 13:30)

**필수**

- [ ] 상품 · 장바구니 · 결제 · 유저 · 셀러를 주문과 같은 4계층 구조로 분리
- [ ] 각 도메인의 `Repository`는 domain에 인터페이스, infrastructure에 구현 (DIP)
- [ ] domain 패키지에 `org.springframework.web`, `jakarta.servlet` import가 **0개**
- [ ] 컨트롤러 공통 응답 `ApiResponse<T>` + `GlobalExceptionHandler`로 에러 응답 형식 통일 (`common/response`, `common/exception`)

**도전**

- [ ] `lectures/ddd/step4-done`의 테스트(`OrderServiceTest`, `OrderControllerTest` 등)를 본인 코드로 가져와 통과시키기
- [ ] ArchUnit으로 "domain은 infrastructure에 의존하지 않는다" 규칙 테스트 추가

**완료 기준**: `./gradlew test` 통과 + 모든 API가 같은 응답 모양(`success`, `data`, `error`)으로 내려옴

### 제출 방법 (공통)

1. 이 레포를 **fork** 한 뒤, 본인 fork에 `hw/ddd-3` 브랜치를 만들어 작업합니다.
2. 마감 전까지 **본인 fork의 브랜치 링크**(또는 PR 링크)를 과제 제출 채널에 올립니다.
3. 다음 날 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 `-done` 브랜치와 나란히 비교합니다.

> 막히면 `lectures/ddd/step4-done` 브랜치와 `git diff`로 비교해 봐도 괜찮습니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
