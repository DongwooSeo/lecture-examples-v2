# [AI ③] RAG 쇼핑 컨시어지와 SSE 스트리밍

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/ai/step2` (구현 전 · 출발 코드) |
| 강의일 | Day 14 · 12/08 (화) 14:00~19:00 |
| 이동 | 이전: `lectures/ai/step1-done` · 다음: `lectures/ai/step2-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/ai/step2
> ```

## 이 브랜치는?

하이브리드 검색까지 완성된 상태입니다(`lectures/ai/step1-done` 과 같은 코드). 오늘은 그 위에 **질문에 답하는 컨시어지**를 올립니다.

> 동기 LLM 호출은 수십 초가 걸려 Gateway 타임아웃(504)에 걸리고 스레드를 붙잡습니다. 그래서 새 서비스는 **WebFlux + SSE** 로 만듭니다.

## 오늘 수업에서 하는 것

| 시간 | 내용 |
| --- | --- |
| 14:00~15:30 | RAG 구조 (검색 → 컨텍스트 구성 → 생성), 환각과 근거 제시, 504 재현 데모 |
| 15:40~19:00 | `concierge-service`(:8087) 신설 — WebClient 로 하이브리드 검색 호출 → ChatClient 로 생성 → `/concierge/ask`, `/concierge/stream` |

## 사전 준비

```bash
ollama list                                   # exaone3.5:2.4b, bge-m3
cd infrastructure && docker compose up -d     # ES (검색 수업 때 그대로)
./gradlew :product-service:bootRun            # :8081 — 하이브리드 검색 API 제공
```

> 어제(12/07) HW15 에서 준비한 **질문 10개**를 오늘 그대로 씁니다.

## 📝 오늘의 과제 (내일 오전)

| 과제 | 마감 | 시작 브랜치 | 검증 |
| --- | --- | --- | --- |
| **HW17** RAG 답변에 근거 상품 붙이기 + 환각 비교 | 12/10 (목) 13:30 | `homework/ai-2` | 브랜치 README 의 완료 기준 |

과제 내용 · 완료 기준 · 제출 방법은 **시작 브랜치의 README** 에 있습니다.
```bash
git fetch origin && git checkout homework/ai-2
```

> 12/09 (수) 오전은 평가 2회차 대비일입니다 (HW16, 선택: SearchIntent 연결).
