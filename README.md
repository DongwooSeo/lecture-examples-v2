# Spring AI와 MSA를 활용한 백엔드 개발 9기 — 강의 예제 코드

하나의 이커머스 모놀리식(상품·장바구니·결제·주문·유저·셀러)을 **DDD로 정리 → 정산 배치 추가 → MSA로 분리 → 검색·AI로 확장**해 가는 강의 예제 레포입니다.
수업은 평일 **14:00~19:00 (온라인)**, 오전에는 **전날 받은 과제**를 진행합니다 (마감 13:30).

> **브랜치 규칙** — `lectures/<주제>/stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 각 브랜치의 README에 그 단계의 목표 · 실행 방법 · 과제가 정리돼 있습니다.

## 📅 브랜치 로드맵

| 강의일 | 과목 | 수업 브랜치 | 다음 날 오전 과제 (시작 브랜치) |
| --- | --- | --- | --- |
| 11/17 (화) | DDD ① 전략적 설계 | `lectures/ddd/step2` | HW01 연관관계 끊기 (`homework/ddd-1`) |
| 11/18 (수) | DDD ② 전술적 설계 | `lectures/ddd/step3` | HW02 장바구니 애그리거트 (`homework/ddd-2`) |
| 11/19 (목) | DDD ③ 4계층 · 도메인 간 소통 | `lectures/ddd/step4`, `step5` | HW03 4계층 + 공통 응답 (`homework/ddd-3`) |
| 11/20 (금) | [특강] 프로젝트 구조 설계 | — | HW04 (주말) 팀 컨텍스트 맵 + OOM 예열 (`ddd/step5` README) |
| 11/23 (월) | 정산 ① 배치 · Chunk · 재시작 | `lectures/settlement/step1` ~ `step3` | HW05 재시작 테스트 + chunk 측정 (`settlement/step3` README) |
| 11/24 (화) | 정산 ② Partitioning / MSA ① | `lectures/settlement/step4`, `lectures/msa/step1` | HW06 측정표 + 멀티 모듈 (`homework/msa-0`) |
| 11/25 (수) | MSA ② Eureka · Gateway · Config | `lectures/msa/step2` ~ `step4` | HW07 나머지 서비스 연결 (`homework/msa-1`) |
| 11/26 (목) | MSA ③ Resilience4j · OpenFeign | `lectures/msa/step5`, `step6` | HW08 cart Feign + 실패 재현 (`homework/msa-2`) |
| 11/27 (금) | MSA ④ Kafka · Saga · Outbox | `lectures/msa/step6-done` 위에서 | HW09 ⭐ (주말) Saga (`homework/msa-saga`) |
| 11/30 (월) | CI/CD ① 컨테이너 · 쿠버네티스 | `lectures/infra/step1` | HW10 Dockerfile + Compose (`homework/infra-1`) |
| 12/01 (화) | CI/CD ② GitHub Actions / 검색 ① | `lectures/infra/step2` | HW11 전 서비스 CI (`homework/infra-2`) |
| 12/02 (수) | 검색 ② Elasticsearch | `lectures/elasticsearch/step1` | HW12 검색 챌린지 · HW14 ⭐ Outbox (`homework/search-outbox`) |
| 12/03 (목) | 검색 ③ / AI ① Spring AI | `lectures/elasticsearch/step1-done` | — (12/04 오후 평가 1회차) |
| 12/07 (월) | AI ② 검색에 AI 통합 | `lectures/ai/step1` | HW15 검색 비교 보고서 (`ai/step1-done` README) |
| 12/08 (화) | AI ③ RAG · SSE | `lectures/ai/step2` | HW16 (선택) · HW17 근거 상품 (`homework/ai-2`) |
| 12/10 (목) | AI ④ LLM 운영 / 세미 프로젝트 킥오프 | — | — |

> 과제 브랜치 `homework/<id>` 에는 과제 README 와 **검증 테스트**(시작할 때는 실패, 다 하면 통과)가 들어 있습니다.
> 정답 예시는 기존 `-done` 브랜치 또는 `homework/<id>-solution` 브랜치입니다.

> 정산 브랜치는 DDD 완성 코드 위에, MSA 브랜치는 정산 완성 코드 위에, 배포·검색·AI 브랜치는 MSA 완성 코드 위에 이어집니다.
> 즉 **하나의 프로젝트가 끝까지 자라는 구조**입니다.

## 🛠 공통 실행 환경

- JDK 21+, Gradle Wrapper 포함 (`./gradlew`)
- DB: 인메모리 H2 (모놀리식 단계) — H2 콘솔 `http://localhost:8080/h2-console`
- MSA 이후: 서비스별 포트 (order 8080 · product 8081 · payment 8082 · user 8083 · seller 8084 · cart 8085 · settlement 8086 · gateway 8000 · discovery 8761 · config 8888)
- 배포 이후: Docker Desktop, 로컬 쿠버네티스 / 검색·AI 이후: Ollama (네이티브 설치)
- AI ③ 이후: concierge 8087

---

# 📝 과제 HW00 — [Day 1 사전 과제] 도메인 분석 (마감 11/17 (화) 14:00)

> Day 1 DDD 강의에 앞서, **앞으로 고도화해 나갈 프로젝트의 도메인을 직접 분석**해보는 사전 과제입니다. 
> 이 과제의 목적은 정답을 맞히는 것이 아니라, 현재 코드가 어떤 방식으로 서로 얽혀 있는지 스스로 파악하고 체감해보는 것입니다. 
> 강의 시간에는 이러한 얽힌 연관관계를 하나씩 분리해 나가며, 도메인 경계를 세우는 과정을 함께 살펴볼 예정입니다.

> (중요) 아직 설계에 익숙하지 않으신 분은 해당 과제가 어려울 수 있습니다. 전혀 걱정하지 않으셔도 됩니다, 강의 때 같이 진행해보고 그래도 이해가 안 가신다면 강의 이후 개인적으로 봐드리겠습니다. 
---

## 📦 분석 대상 도메인

현재 프로젝트는 **하나의 모놀리식 프로젝트**에 아래 6개 도메인이 섞여 있습니다.

| 도메인 | 주요 Entity |
| --- | --- |
| 유저 (User) | `User` |
| 셀러 (Seller) | `Seller` |
| 상품 (Product) | `Product` |
| 장바구니 (Cart) | `Cart`, `CartItem` |
| 주문 (Order) | `Order`, `OrderItem` |
| 결제 (Payment) | `Payment` |

각 Entity는 다음 경로에 있습니다.

```
src/main/java/com/growmighty/lectures/firstday/tangledmonolith/
├── user/User.java
├── seller/Seller.java
├── product/Product.java
├── cart/Cart.java
├── cart/CartItem.java
├── order/Order.java
├── order/OrderItem.java
└── payment/Payment.java
```

---

## ✏️ 과제 1. 도메인 연관관계 다이어그램 그리기 (draw.io)

### 무엇을 하나요?

현재 구현된 **Entity 코드(특히 JPA 연관관계 어노테이션)를 직접 읽고**,
Entity 간의 연관관계를 **[draw.io](https://app.diagrams.net/)** 로 다이어그램으로 표현해주세요.

### 어떻게 그리나요? (예시)

아래는 **다른 도메인의 예시**입니다. 우리 프로젝트와 모양은 다르지만, **이런 식으로** 그려주시면 됩니다.

- 각 Entity를 **네모 박스**로 그리고, 한글명과 영문 클래스명을 함께 적기 (예: `주문 (Order)`)
- Entity 사이를 **선으로 연결**하고, 그 선 위에 연관관계 종류를 표기
  - `@OneToOne`, `@OneToMany`, `@ManyToOne`, `@ManyToMany`
- **방향(누가 누구를 참조하는지)** 과 **다중성(1, N)** 을 화살표 / 까치발 표기로 드러내기

> 💡 예시 이미지처럼 "박스 + 연결선 + 연관관계 어노테이션 라벨" 형태면 충분합니다.
> 디자인보다 **연관관계를 정확히 읽어내는 것**이 중요합니다.

<img width="853" height="592" alt="스크린샷 2026-06-26 오전 10 50 15" src="https://github.com/user-attachments/assets/9f0b997b-6bdd-4456-8665-474fed3bef6a" />

> ⚠️ 위 이미지는 **참고용 예시**이며, 우리 프로젝트의 실제 도메인과는 다릅니다. 실제 코드를 보고 직접 그려주세요.

### 분석 시 꼭 확인할 포인트

코드를 읽을 때 아래를 단서로 삼으세요.

- 필드에 붙은 `@OneToOne` / `@OneToMany` / `@ManyToOne` 어노테이션
- `@JoinColumn(name = "...")` → **외래키(FK)를 누가 들고 있는지**
- `mappedBy = "..."` → **양방향 관계인지, 연관관계의 주인이 누구인지**
- `cascade`, `orphanRemoval`, `fetch` 옵션 → **함께 묶여 움직이는 객체 묶음(힌트!)**

### 제출물

- draw.io 다이어그램 파일(`.drawio`) **또는** 내보낸 이미지(`.png` / `.jpg`)

---

## ✏️ 과제 2. 현재 코드의 문제점 서술하기 (주관식)

### 무엇을 하나요?

과제 1에서 그린 다이어그램을 보면서,
**"지금 이 코드, 이대로 괜찮을까?"** 를 스스로 질문해보고 자유롭게 서술해주세요.

> 정답은 없습니다. **느낀 점, 불편한 점, 위험해 보이는 점**을 솔직하게 적어주세요.
> 수업에서 다룰 내용을 미리 고민해보는 것이 목적입니다.

### 이런 관점에서 생각해보세요 (가이드 질문)

아래 질문은 **생각을 돕기 위한 힌트**일 뿐, 전부 답할 필요는 없습니다.

1. **연관관계의 방향과 결합도**
   - 한 Entity가 다른 도메인의 Entity를 **객체로 직접 참조**하고 있나요?
   - 예를 들어 `OrderItem`이 `Product`를 직접 들고 있을 때, 어떤 문제가 생길 수 있을까요?
   - 한 도메인을 수정하면 다른 도메인까지 영향을 받게 되지는 않나요?

2. **도메인 경계**
   - 지금 6개 도메인은 서로 **명확히 구분**되어 있나요, 아니면 **거미줄처럼 얽혀** 있나요?
   - "이 객체는 어느 도메인의 것인가?"를 명확히 말할 수 있나요?

3. **데이터 정합성 / 변경에 대한 취약성**
   - `OrderItem`이 `Product`를 참조하는데, **주문 이후 상품 가격이 바뀌면** 어떻게 될까요?
   - 주문 당시의 정보는 어떻게 보존되어야 할까요?

4. **객체로 직접 참조 vs ID로 간접 참조**
   - 지금처럼 객체로 직접 참조하는 방식의 **장점과 단점**은 무엇일까요?
   - 만약 도메인을 나중에 **별도 서비스로 분리**한다면 어떤 문제가 생길까요?

5. **(선택) 비즈니스 로직의 위치**
   - 도메인 규칙(재고 차감, 금액 계산 등)은 지금 **어디에** 작성되어 있나요?
   - Entity는 단순히 데이터만 담는 그릇 역할만 하고 있지는 않나요?
   
6. **(선택) 이외에 코드 수정이 필요하다고 느끼는 부분을 자유롭게 서술해주세요!** 

### 제출물

- 자유 형식 문서 (`.md`, `.txt`, `.docx`, `.pdf` 등 무엇이든 OK)
- 분량 제한 없음 — **3~5개 문제점**을 골라 각각 2~3줄씩만 적어도 충분합니다.

---

## 📮 제출 안내

### 👉 제출은 아래 구글 폼으로 받습니다

> ⚠️ 아래 폼 링크는 7기 것입니다. 9기 폼으로 교체해 주세요.

### **[📝 사전 과제 제출하기 (Google Form)](https://docs.google.com/forms/d/e/1FAIpQLSexj6GrUcdo92huwJLHpjPLVHi5ymdxCf1HPLmFvTV1AMBn_Q/viewform)**

- **제출 기한**: Day 1 수업 시작 전까지
- **제출물 2종** (폼에서 한 번에 제출)
  1. 도메인 연관관계 다이어그램 (draw.io 파일 또는 이미지) → **파일 업로드**
  2. 코드 문제점 서술 → **폼에 직접 작성** (또는 문서 파일 업로드)
- ⚠️ 파일 업로드를 위해 **구글 계정 로그인**이 필요합니다.
- 완벽하지 않아도 됩니다. **직접 코드를 읽고 고민한 흔적**이 가장 중요합니다. 💪

> 이 과제에서 느낀 "얽힘"과 "불편함"을, 수업에서 **하나씩 끊어내며 해결**해 나갈 예정입니다.
> 가벼운 마음으로, 그러나 진지하게 코드를 들여다봐 주세요!
