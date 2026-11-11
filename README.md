# [MSA ②-1] 서비스 디스커버리 — 하드코딩된 주소와 작별하기

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/msa/step2` (구현 전 · 출발 코드) |
| 강의일 | Day 6 · 11/25 (수) 14:00~ |
| 이동 | 이전: `lectures/msa/step1-done` · 다음: `lectures/msa/step2-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/msa/step2
> ```

## 오늘 수업 (11/25) 흐름

> 서비스를 쪼갰다 → 서로의 주소를 모른다(**Eureka**) → 진입점이 흩어진다(**Gateway**) → 설정이 사방에 흩어진다(**Config**)

| Step | 브랜치 | 내용 |
| --- | --- | --- |
| 2 | `lectures/msa/step2` | 서비스 디스커버리 (Spring Cloud Netflix Eureka) |
| 3 | `lectures/msa/step3` | API Gateway (Spring Cloud Gateway) |
| 4 | `lectures/msa/step4` | 중앙 설정 관리 (Spring Cloud Config) |

수업에서는 **order · product**에 먼저 적용하고, 나머지 서비스는 과제 7로 이어집니다.

## 이 브랜치는?

멀티 모듈로 나뉜 7개 서비스가 서로를 `http://localhost:8081` 같은 **하드코딩된 주소**로 호출합니다.
포트가 바뀌거나 인스턴스가 늘어나면 호출하는 쪽 코드를 고쳐야 합니다.

## Step 2에서 할 것

- [ ] `discovery-server` 모듈 추가 (`@EnableEurekaServer`, :8761)
- [ ] 각 서비스를 Eureka Client로 등록 (`spring.application.name`)
- [ ] 호출 쪽 RestClient를 `@LoadBalanced`로 → `http://product-service`처럼 **이름으로** 호출
- [ ] Eureka 대시보드 `http://localhost:8761`에서 등록 확인
