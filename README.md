# [MSA 예열] 모놀리식을 멀티 모듈로 쪼개기 — 오전 과제 브랜치

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/msa/step1` (구현 전 · 출발 코드) |
| 강의일 | 11/25 (수) 오전 과제 → 14:00 MSA ② 시작 전 |
| 이동 | 이전: `lectures/settlement/step4-done` · 다음: `lectures/msa/step1-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/msa/step1
> ```

## 이 브랜치는?

정산까지 완성된 **하나의 모놀리식**입니다. 오늘 오후 MSA 수업은 이 프로젝트가 **서비스별 모듈로 나뉜 상태**에서 시작합니다.
멀티 모듈 전환은 기계적인 작업이 많아, 라이브 대신 **오전 과제**로 진행합니다.

> 11/24 (화) 18:00 MSA ① "왜 MSA인가" 시간에 전체 방향을 함께 보고 시작합니다.

## 📝 과제 6-B — 멀티 모듈 분리 (마감 11/25 (수) 13:30)

**목표 구조**

```
settings.gradle
common/              ← ApiResponse, 공통 예외 등 공용 코드 (실행 X)
product-service/     :8081
payment-service/     :8082
user-service/        :8083
seller-service/      :8084
cart-service/        :8085
settlement-service/  :8086
order-service/       :8080
```

**필수**

- [ ] 루트 `settings.gradle`에 `common` + 서비스 7개 모듈 등록
- [ ] 도메인 패키지를 해당 모듈로 이동, 각 모듈에 `*ServiceApplication` 메인 클래스와 포트 설정
- [ ] **각 서비스는 자기 DB만** 사용 (H2를 서비스별로 분리)
- [ ] 다른 도메인 호출은 DDD Step 5의 **API 호출(Port + HttpClient)** 방식으로 — 직접 빈 호출 금지
- [ ] 7개 서비스가 각자 기동되고, `order-service`로 주문 생성이 성공

**도전**

- [ ] 모듈 간 순환 의존이 없는지 `./gradlew :order-service:dependencies`로 확인
- [ ] 기존 테스트를 각 모듈로 옮겨 `./gradlew test` 통과

**힌트**: DDD 4계층 + Port 구조를 제대로 지켰다면, 패키지 단위로 그대로 들어 옮기면 됩니다. 옮기기 어려운 곳이 있다면 그곳이 **경계가 새는 곳**입니다.

### 제출 방법 (공통)

1. 이 레포를 **fork** 한 뒤, 본인 fork에 `hw/msa-0` 브랜치를 만들어 작업합니다.
2. 마감 전까지 **본인 fork의 브랜치 링크**(또는 PR 링크)를 과제 제출 채널에 올립니다.
3. 다음 날 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 `-done` 브랜치와 나란히 비교합니다.

> 막히면 `lectures/msa/step1-done` 브랜치와 `git diff`로 비교해 봐도 괜찮습니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
