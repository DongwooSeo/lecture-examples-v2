# [AI ②] 완성 — AI 색인 파이프라인 + 하이브리드 검색

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/ai/step1-done` (구현 후 · 완성 코드) |
| 강의일 | Day 13 · 12/07 (월) |
| 이동 | 이전: `lectures/ai/step1` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/ai/step1-done
> ```

## 이 브랜치에서 달라진 것

| 구성 | 위치 | 설명 |
| --- | --- | --- |
| AI 설정 | `product-service/src/main/resources/application.yml` | `spring.ai.model.chat/embedding` 스위치, Ollama · OpenAI |
| 속성 추출 | `infrastructure/ai/ProductEnrichService`, `ProductAttributes` | Structured Output + 임베딩 |
| 검색 의도 | `infrastructure/ai/SearchIntent` | 검색어 구조화용 타입 |
| Port | `application/port/ProductIndexPort`, `ProductSearchPort` | DIP |
| 어댑터 | `infrastructure/search/ProductIndexAdapter`, `ProductSearchAdapter` | dense_vector 색인, kNN, RRF |
| API | `ProductSearchController` | `/products/search`, `/semantic`, `/hybrid` |

```http
GET http://localhost:8081/products/search?keyword=하객룩 원피스
GET http://localhost:8081/products/search/semantic?keyword=하객룩 원피스
GET http://localhost:8081/products/search/hybrid?keyword=하객룩 원피스
GET http://localhost:8081/products/search/hybrid?keyword=MX Master 3S
```

> OpenAI로 전환할 때 API 키는 환경변수 `OPENAI_API_KEY`로만 — **절대 커밋 금지**.

## 📝 과제 10 — 검색 방식 비교 보고서 + RAG 준비 (마감 12/08 (화) 13:30)

**필수**

- [ ] 검증 세트 5개(정확 매칭 2 · 서술형 2 · 신조어 1)를 정하고 keyword / semantic / hybrid로 실행
- [ ] 상위 3개 결과를 비교표로 정리 — **하이브리드가 오히려 불리한 케이스**를 1개 이상 찾아 이유 분석
- [ ] 내일 RAG 쇼핑 컨시어지에 던질 **사용자 질문 10개** 준비 (예: "결혼식 하객으로 입을 10만원대 원피스 추천해줘")

**도전**

- [ ] `SearchIntent`를 실제 검색에 연결 — LLM으로 검색어를 구조화해 kNN filter를 정밀화하고, 추가 지연(ms)과 비용을 측정

### 제출 방법 (공통)

1. 이 레포를 **fork** 한 뒤, 본인 fork에 `hw/ai-1` 브랜치를 만들어 작업합니다.
2. 마감 전까지 **본인 fork의 브랜치 링크**(또는 PR 링크)를 과제 제출 채널에 올립니다.
3. 다음 날 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 `-done` 브랜치와 나란히 비교합니다.

> 막히면 `lectures/ai/step1-done` 브랜치와 `git diff`로 비교해 봐도 괜찮습니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.

> 다음 AI ③ (RAG · SSE 스트리밍) 브랜치는 준비 중입니다.
