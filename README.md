# [CI/CD ②] 완성 — order-service CI 워크플로

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/infra/step2-done` (구현 후 · 완성 코드) |
| 강의일 | Day 10 · 12/01 (화) |
| 이동 | 이전: `lectures/infra/step2` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/infra/step2-done
> ```

## 이 브랜치에서 달라진 것

- `.github/workflows/order-service.yml` — `order-service/**`, `common/**` 이 바뀔 때만 실행
  - 테스트 → bootJar → (main 일 때만) GHCR 로그인 → `ghcr.io/<owner>/order-service:<SHA>`, `:latest` push
  - GHCR 이미지 이름은 소문자만 → `${GITHUB_REPOSITORY_OWNER,,}`

## 확인

본인 fork 의 main 에 `order-service` 를 고친 커밋을 push → Actions 탭 녹색 체크 → Packages 에 이미지.

## 📝 과제와의 관계

**과제 HW11** (`homework/infra-2`)의 정답 예시는 `homework/infra-2-solution` 브랜치에 있습니다.
