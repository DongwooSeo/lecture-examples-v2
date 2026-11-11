# [MSA ②-2] API Gateway — 문은 하나면 됩니다

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/msa/step3` (구현 전 · 출발 코드) |
| 강의일 | Day 6 · 11/25 (수) |
| 이동 | 이전: `lectures/msa/step2-done` · 다음: `lectures/msa/step3-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/msa/step3
> ```

## 이 브랜치는?

서비스는 이름으로 서로를 찾지만, **클라이언트는 여전히 서비스마다 다른 포트**로 들어와야 합니다.

## Step 3에서 할 것

- [ ] `gateway-server` 모듈 추가 (Spring Cloud Gateway, :8000)
- [ ] 라우팅 규칙 작성: `/orders/**` → `lb://order-service`, `/products/**` → `lb://product-service` …
- [ ] 모든 요청을 `http://localhost:8000` 한 곳으로 보내기
