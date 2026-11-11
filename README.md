# [AI ③] 완성 — concierge-service (RAG + WebFlux SSE)

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/ai/step2-done` (구현 후 · 완성 코드) |
| 강의일 | Day 14 · 12/08 (화) |
| 이동 | 이전: `lectures/ai/step2` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/ai/step2-done
> ```

## 이 브랜치에서 달라진 것

| 구성 | 위치 | 설명 |
| --- | --- | --- |
| 모듈 | `settings.gradle`, `concierge-service/build.gradle` | WebFlux + Spring AI Ollama, 포트 8087 |
| R (검색) | `client/ProductSearchClient` | WebClient 로 product-service `/products/search/hybrid` 호출 (top-k 5) |
| A (컨텍스트) | `application/ConciergeService#userPrompt` | 후보 상품을 `[상품 N] 이름 · 가격 · 상황 · 스타일 …` 한 줄씩 |
| G (생성) | `application/ConciergeService` | 시스템 프롬프트로 "목록 안의 상품만" 제약, `call()` / `stream()` |
| API | `presentation/ConciergeController` | `POST /concierge/ask`, `GET /concierge/stream` (SSE: `token` … `done`) |

## 실행

```bash
./gradlew :product-service:bootRun      # :8081
./gradlew :concierge-service:bootRun    # :8087
curl -N "http://localhost:8087/concierge/stream?question=결혼식 하객으로 입을 10만원대 원피스 추천해줘"
```

요청 모음: `concierge-service/concierge.http`

> 응답에 **근거 상품**은 아직 없습니다 → 과제 HW17 (`homework/ai-2`).

## 📝 과제와의 관계

**과제 HW17** (`homework/ai-2`)의 정답 예시는 `homework/ai-2-solution` 브랜치에 있습니다.
