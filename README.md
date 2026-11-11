# 📝 과제 HW17 — RAG 답변에 근거 상품 붙이기 + 환각 비교

| 항목 | 내용 |
| --- | --- |
| 마감 | **12/10 (목) 13:30** |
| 유형 | 복습 · 예상 2시간 |
| 시작 브랜치 | `homework/ai-2` (이 브랜치) |
| 정답 예시 | `homework/ai-2-solution` |
| 이어지는 수업 | Day 14 · AI ③ RAG 와 스트리밍 (12/08) |

## 왜 하나요?

컨시어지가 "이 원피스 추천해요"라고 말해도, 사용자는 그게 **실제로 있는 상품인지** 확인할 방법이 없습니다.
LLM 에게 근거로 넘긴 상품을 응답에 함께 돌려주면, 화면에서 바로 상품 카드로 보여 주고 답변을 검증할 수 있습니다.

## 할 일

**필수**

- [ ] `ConciergeAnswer` 에 `sources` — 근거로 넘긴 상품 목록 (각 항목에 `id`, `name`)
- [ ] `POST /concierge/ask` 응답에 `sources` 포함
- [ ] `GET /concierge/stream` 은 토큰보다 **먼저** `event: sources` 를 한 번 보내기 (data 에 상품 ID 들) → `token` … → `done`
- [ ] 검색은 한 번만 — 같은 근거로 sources 와 답변을 만들 것
- [ ] **환각 비교 보고서**: 12/08 에 준비한 질문 중 3개를 ① 검색 없이 LLM 에게 바로 ② RAG 로 물어보고, 지어낸 상품·가격이 있는지 표로 비교

**도전**

- [ ] 답변에 등장한 `[상품 N]` 중 sources 에 없는 번호를 찾아 경고 로그 남기기 (환각 감지)
- [ ] `concierge-service` 를 Gateway 라우트에 추가하고 Gateway 경유 SSE 가 끊기지 않는지 확인

## 완료 기준 — 검증 테스트

```bash
./gradlew :concierge-service:test --tests '*ConciergeGroundingTest*'
```

실제 LLM · 검색 없이 Mock 으로 돕니다. `/ask` 응답의 sources ID, 후보가 없을 때 빈 sources, 스트림의 첫 이벤트가 `sources` 인지 검사합니다.

## 제출 방법

1. 이 레포를 **fork** 하고, 본인 fork 에서 이 브랜치를 기준으로 `hw/ai-2` 브랜치를 만듭니다.
   ```bash
   git fetch upstream && git checkout -b hw/ai-2 upstream/homework/ai-2
   ```
2. **마감(12/10 (목) 13:30) 전까지** 본인 fork 의 브랜치(또는 PR) 링크를 과제 제출 채널에 올립니다.
3. 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 정답 브랜치와 나란히 비교합니다.

> 막히면 정답 브랜치 `homework/ai-2-solution` 와 `git diff` 로 비교해 봐도 됩니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
