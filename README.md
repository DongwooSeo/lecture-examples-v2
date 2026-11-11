# ✅ 과제 HW11 정답 예시

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `homework/infra-2-solution` |
| 과제 | `homework/infra-2` 브랜치 README |

> 정답은 하나가 아닙니다. 이 브랜치는 **검증 테스트를 통과하는 한 가지 방법**입니다.
> 본인 코드와 비교할 때는 `git diff homework/infra-2 homework/infra-2-solution` 로 무엇이 바뀌었는지부터 보세요.

## 바뀐 것

- `.github/workflows/_service-ci.yml` — `workflow_call` 재사용 워크플로 (테스트 → bootJar → GHCR, PR 에서는 테스트만)
- 서비스 10개 워크플로 — `paths` 필터 + `uses: ./.github/workflows/_service-ci.yml`

## 확인

과제 브랜치의 완료 기준 명령을 그대로 실행하면 모두 통과합니다.
