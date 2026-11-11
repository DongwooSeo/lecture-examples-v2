# [MSA ②-1] 완성 — Eureka 서비스 디스커버리

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/msa/step2-done` (구현 후 · 완성 코드) |
| 강의일 | Day 6 · 11/25 (수) |
| 이동 | 이전: `lectures/msa/step2` · 다음: `lectures/msa/step3` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/msa/step2-done
> ```

## 이 브랜치에서 달라진 것

- `discovery-server` 모듈 (:8761)
- 모든 서비스가 Eureka Client로 등록
- `order-service`, `cart-service`의 클라이언트 설정(`*ClientConfig`)이 서비스 이름 기반 호출로 변경

## 실행

```bash
./gradlew :discovery-server:bootRun    # 먼저
./gradlew :product-service:bootRun
./gradlew :order-service:bootRun
open http://localhost:8761
```

다음 단계는 `lectures/msa/step3`입니다.
