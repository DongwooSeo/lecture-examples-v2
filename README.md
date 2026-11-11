# ✅ 과제 HW10 정답 예시

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `homework/infra-1-solution` |
| 과제 | `homework/infra-1` 브랜치 README |

> 정답은 하나가 아닙니다. 이 브랜치는 **검증 테스트를 통과하는 한 가지 방법**입니다.
> 본인 코드와 비교할 때는 `git diff homework/infra-1 homework/infra-1-solution` 로 무엇이 바뀌었는지부터 보세요.

## 바뀐 것

- 서비스 4개 Dockerfile 추가 (10개 완성)
- `docker-compose.yml` — `x-client` 앵커로 반복 제거, config · discovery 에 `/dev/tcp` 포트 healthcheck, `condition: service_healthy`

## 확인

과제 브랜치의 완료 기준 명령을 그대로 실행하면 모두 통과합니다.
