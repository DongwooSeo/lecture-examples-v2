# [CI/CD ②] GitHub Actions 파이프라인

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/infra/step2` (구현 전 · 출발 코드) |
| 강의일 | Day 10 · 12/01 (화) 14:00~17:00 |
| 이동 | 이전: `lectures/infra/step1-done` · 다음: `lectures/infra/step2-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/infra/step2
> ```

## 이 브랜치는?

서비스 10개가 모두 이미지로 만들어지고, Compose 로 한 번에 뜨는 상태입니다(과제 HW10 정답과 같은 코드).
하지만 이미지는 아직 **내 컴퓨터에서 손으로** 만듭니다.

## 오늘 수업에서 하는 것

> push 한 번 → 테스트 → jar → 이미지 → 레지스트리(GHCR)

1. GitHub Actions 기본: workflow · job · step, `on: push` + `paths` 필터
2. `order-service` 워크플로 작성: checkout → JDK 21 → Gradle 캐시 → 테스트 → bootJar → GHCR 로그인 → build & push
3. PR 에서는 테스트만, main 에서만 이미지 push
4. 이미지 태그를 커밋 SHA 로 → 쿠버네티스 롤링 업데이트 → 배포 실패 시 롤백 (`kubectl rollout undo`)

## 사전 준비

- 본인 GitHub 계정으로 이 레포 fork (Actions 탭 활성화)

## 📝 오늘의 과제 (내일 오전)

| 과제 | 마감 | 시작 브랜치 | 검증 |
| --- | --- | --- | --- |
| **HW11** 전 서비스 CI — 재사용 워크플로 + 역색인 예열 | 12/02 (수) 13:30 | `homework/infra-2` | 브랜치 README 의 완료 기준 |

과제 내용 · 완료 기준 · 제출 방법은 **시작 브랜치의 README** 에 있습니다.
```bash
git fetch origin && git checkout homework/infra-2
```

> 마지막 2시간(17:00~19:00)은 검색 ① 시간입니다 — `lectures/elasticsearch/step1` README 참고.
