# 📝 과제 HW06-B — 멀티 모듈 분리 — product · payment · order 이관

| 항목 | 내용 |
| --- | --- |
| 마감 | **11/25 (수) 13:30** |
| 유형 | 예열 · 예상 2.5시간 |
| 시작 브랜치 | `homework/msa-0` (이 브랜치) |
| 정답 예시 | `lectures/msa/step1-done` |
| 이어지는 수업 | Day 6 · MSA ② Spring Cloud (11/25 14:00) |

## 왜 하나요?

오늘 오후 MSA 수업은 프로젝트가 **서비스별 모듈로 나뉜 상태**에서 시작합니다.
기계적인 작업이 많아 라이브 대신 오전 과제로 진행합니다. 분량을 줄이려고 4개 서비스는 미리 옮겨 두었습니다.

| 모듈 | 상태 |
| --- | --- |
| `common`, `user-service`, `seller-service`, `cart-service`, `settlement-service` | ✅ 이관 완료 (참고용) |
| `product-service` :8081, `payment-service` :8082, `order-service` :8080 | ⬜ **빈 껍데기** — `build.gradle`, `*ServiceApplication`, `application.properties` 만 있음 |
| `monolith-reference/src/...` | 옮겨 올 모놀리식 원본 코드 (빌드에 포함되지 않음) |
| `monolith-reference/tests/<모듈>/` | 이관이 끝나면 각 모듈 `src/test` 로 옮길 테스트 |

## 할 일

**필수**

- [ ] `monolith-reference/src` 의 `product`, `payment`, `order` 코드를 각 모듈의 `com.growmighty.lectures.firstday.<도메인>` 패키지로 이관
- [ ] 패키지 경로가 `...tangledmonolith.<도메인>` → `...firstday.<도메인>` 으로 바뀝니다. 공통 응답·예외는 `common` 모듈의 것을 사용
- [ ] **각 서비스는 자기 DB만** — 다른 도메인 Repository/Service 를 직접 호출하던 곳은 DDD Step 5 의 **API 호출(Port + HttpClient)** 로
  - order → product (`GET /products/{id}`, `POST /products/{id}/decrease-stock`), order → payment (`POST /payments`)
- [ ] `monolith-reference/tests/<모듈>/java` 를 각 모듈 `src/test/java` 로 옮겨 통과

**도전**

- [ ] 서비스 3개를 띄우고 `order-service` 로 주문 생성 성공 (`curl -X POST localhost:8080/orders ...`)
- [ ] `./gradlew :order-service:dependencies` 로 order 가 product/payment 모듈에 **컴파일 의존하지 않음**을 확인

## 완료 기준

```bash
./gradlew build      # 7개 서비스 모듈 컴파일 + 옮긴 테스트 모두 통과
```

> 힌트: DDD 4계층 + Port 구조를 지켰다면 패키지째 들어 옮기면 됩니다. 옮기기 어려운 곳이 있다면, 그곳이 **경계가 새는 곳**입니다.

## 제출 방법

1. 이 레포를 **fork** 하고, 본인 fork 에서 이 브랜치를 기준으로 `hw/msa-0` 브랜치를 만듭니다.
   ```bash
   git fetch upstream && git checkout -b hw/msa-0 upstream/homework/msa-0
   ```
2. **마감(11/25 (수) 13:30) 전까지** 본인 fork 의 브랜치(또는 PR) 링크를 과제 제출 채널에 올립니다.
3. 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 정답 브랜치와 나란히 비교합니다.

> 막히면 정답 브랜치 `lectures/msa/step1-done` 와 `git diff` 로 비교해 봐도 됩니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
