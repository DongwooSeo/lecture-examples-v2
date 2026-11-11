# [MSA ②-2] 완성 — API Gateway로 전체 도메인 라우팅

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/msa/step3-done` (구현 후 · 완성 코드) |
| 강의일 | Day 6 · 11/25 (수) |
| 이동 | 이전: `lectures/msa/step3` · 다음: `lectures/msa/step4` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/msa/step3-done
> ```

## 이 브랜치에서 달라진 것

- `gateway-server` 모듈과 전체 도메인 라우팅 (`gateway-server/src/main/resources/application.yml`)

## 실행 순서

```bash
./gradlew :discovery-server:bootRun
./gradlew :gateway-server:bootRun
./gradlew :product-service:bootRun   # 필요한 서비스들
curl http://localhost:8000/products/1
```

다음 단계는 `lectures/msa/step4`입니다.
