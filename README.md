# ✅ 과제 HW17 정답 예시

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `homework/ai-2-solution` |
| 과제 | `homework/ai-2` 브랜치 README |

> 정답은 하나가 아닙니다. 이 브랜치는 **검증 테스트를 통과하는 한 가지 방법**입니다.
> 본인 코드와 비교할 때는 `git diff homework/ai-2 homework/ai-2-solution` 로 무엇이 바뀌었는지부터 보세요.

## 바뀐 것

- `ConciergeAnswer.sources` (`Source(id, name)`)
- `ConciergeService.retrieve()` / `generate()` 로 검색과 생성을 분리 → 같은 근거를 응답과 스트림에 재사용
- `/concierge/stream` — `sources` 이벤트(상품 ID 콤마 구분) → `token` … → `done`

## 확인

과제 브랜치의 완료 기준 명령을 그대로 실행하면 모두 통과합니다.
