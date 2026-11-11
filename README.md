# [CI/CD ①] 컨테이너와 쿠버네티스

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/infra/step1` (구현 전 · 출발 코드) |
| 강의일 | Day 9 · 11/30 (월) 14:00~19:00 |
| 이동 | 이전: `lectures/msa/step6-done` · 다음: `lectures/infra/step1-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/infra/step1
> ```

## 이 브랜치는?

MSA 완성 코드(`lectures/msa/step6-done`)와 같습니다. 서비스 10개를 띄우려면 터미널 10개와 정확한 기동 순서가 필요한 상태입니다.

> "제 컴퓨터에서는 되는데요"를 없애고, 명령어 한 줄로 전체를 띄우는 것이 오늘의 목표입니다.

## 오늘 수업에서 하는 것

| 시간 | 내용 |
| --- | --- |
| 14:00~16:30 | Docker: 이미지 / 컨테이너 / Dockerfile, 컨테이너 vs VM → `order-service` Dockerfile → `docker build` · `run` → Compose 로 인프라 3종 + 서비스 3개 |
| 16:40~19:00 | Compose 의 한계 4가지 → 쿠버네티스: 선언형 · 자가 치유, Pod · Deployment · Service · Ingress, ConfigMap, Probe → Spring Cloud → K8s 대체표, ECS 대응표 |

## 사전 준비

- Docker Desktop
- 로컬 쿠버네티스 (Docker Desktop 의 Kubernetes 켜기 또는 kind · minikube 중 공지된 것 하나)

## 핵심 포인트 — 컨테이너 안의 localhost

컨테이너 안에서 `localhost` 는 **자기 자신**입니다. config-server · Eureka 주소를 서비스 이름으로 바꿔 주입해야 합니다.

```yaml
SPRING_CONFIG_IMPORT: optional:configserver:http://config-server:8888
SPRING_APPLICATION_JSON: '{"eureka":{"client":{"service-url":{"defaultZone":"http://discovery-server:8761/eureka"}}}}'
```

## 📝 오늘의 과제 (내일 오전)

| 과제 | 마감 | 시작 브랜치 | 검증 |
| --- | --- | --- | --- |
| **HW10** 10개 서비스 Dockerfile + Compose 로 전체 기동 | 12/01 (화) 13:30 | `homework/infra-1` | 브랜치 README 의 완료 기준 |

과제 내용 · 완료 기준 · 제출 방법은 **시작 브랜치의 README** 에 있습니다.
```bash
git fetch origin && git checkout homework/infra-1
```
