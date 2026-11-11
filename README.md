# [AI ②] 검색 파이프라인에 AI 통합 — 하이브리드 검색

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/ai/step1` (구현 전 · 출발 코드) |
| 강의일 | Day 13 · 12/07 (월) 14:00~19:00 |
| 이동 | 이전: `lectures/elasticsearch/step1-done` · 다음: `lectures/ai/step1-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/ai/step1
> ```

## 이 브랜치는?

ES 검색이 완성된 상태입니다. 그런데 패션 카테고리가 들어오자 **"하객룩 원피스"** 같은 검색어에 결과가 비어 있습니다. 동의어 사전으로는 끝이 없습니다.

> 오늘의 핵심은 AI 기능이 아니라 **"느리고, 비싸고, 실패할 수 있는 외부 시스템(LLM)을 파이프라인에 안전하게 통합하는 방법"** 입니다.

## 사전 준비

```bash
ollama pull exaone3.5:2.4b   # 챗 모델
ollama pull bge-m3           # 임베딩 모델 (1,024차원)
ollama list
cd infrastructure && docker compose up -d   # ES는 검색 수업 때 그대로
```

## 오늘 수업에서 하는 것

| Step | 내용 |
| --- | --- |
| 0 | 환경 기동 + "하객룩 원피스" 검색 결과 스크린샷 (Before) |
| 1 | 색인 파이프라인에 속성 추출(Structured Output) + 임베딩, `temperature: 0.0`, **부분 실패 정책** — Ollama 중단 시에도 상품 등록은 성공 |
| 2 | `ProductIndexPort` + 어댑터(DIP), `reindexAll`로 임베딩 **백필**, yml로 ollama ↔ openai 전환 |
| 3 | kNN 단독(`/semantic`)의 강점과 한계 → **RRF 하이브리드**(`/hybrid`) Before / After |
