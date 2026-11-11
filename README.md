# [CI/CD ①] 완성 — Dockerfile · Compose · 쿠버네티스 매니페스트

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/infra/step1-done` (구현 후 · 완성 코드) |
| 강의일 | Day 9 · 11/30 (월) |
| 이동 | 이전: `lectures/infra/step1` · 다음: `lectures/infra/step2` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/infra/step1-done
> ```

## 이 브랜치에서 달라진 것

| 구성 | 위치 | 설명 |
| --- | --- | --- |
| 이미지 | `config-server`, `discovery-server`, `gateway-server`, `order-service`, `product-service`, `payment-service` 의 `Dockerfile` | `eclipse-temurin:21-jre` + bootJar 복사 |
| Compose | `docker-compose.yml` | 6개 컨테이너, `x-client-env` 앵커로 config · Eureka 주소 주입 |
| 쿠버네티스 | `k8s/order-service.yaml` | ConfigMap · Deployment(2 replicas, readiness/liveness) · Service · Ingress |

## 실행

```bash
./gradlew bootJar
docker compose up -d --build
docker compose ps
curl http://localhost:8000/products/1

# 쿠버네티스 (이미지를 로컬 클러스터에 올린 뒤)
kubectl apply -f k8s/order-service.yaml
kubectl get pods,svc,ingress
kubectl delete pod -l app=order-service    # 지워도 다시 2개가 된다 = 자가 치유
```

## 📝 과제와의 관계

**과제 HW10** (`homework/infra-1`)의 정답 예시는 `homework/infra-1-solution` 브랜치에 있습니다.


다음 수업은 `lectures/infra/step2`에서 시작합니다.
