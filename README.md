# [검색 ②] product-service에 Elasticsearch 검색 붙이기

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/elasticsearch/step1` (구현 전 · 출발 코드) |
| 강의일 | Day 11 · 12/02 (수) 14:00~19:00 |
| 이동 | 이전: `lectures/msa/step6-done` · 다음: `lectures/elasticsearch/step1-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/elasticsearch/step1
> ```

## 이 브랜치는?

MSA 완성 코드입니다. 상품 검색은 아직 DB `LIKE '%키워드%'` 수준입니다 (12/01 검색 ① 시간에 그 한계 5가지를 확인했습니다).

> 오늘의 진짜 주제는 검색 기능이 아니라 **"DB와 ES, 두 저장소를 어긋나지 않게 데려가는 법"** 입니다.

## 오늘 수업에서 하는 것

> nori가 든 ES를 띄운다 → 분석기 3개를 조립한다 → DB 변경을 ES로 흘려보낸다 → 검색·랭킹이 된다 → 그런데 ES가 죽으면? → 재색인으로 복구한다

1. 인프라: nori 설치된 ES + Kibana (`infrastructure/docker-compose.yml`)
2. `ProductDocument`(read model) + Repository, `_id = productId` 멱등 설계
3. 인덱스 설계: 동의어(검색 시) + 자동완성 edge_ngram(색인 시)
4. 색인 파이프라인: `AFTER_COMMIT` 이벤트 + 전체 재색인
5. 검색 API: multi_match + 필터 + function_score 랭킹
6. **정합성 장애 실험**: ES 중단 → 상품 등록 성공 → 검색엔 없음 → 재색인으로 복구

## 사전 준비

```bash
cd infrastructure && docker compose up -d --build   # ES 9200, Kibana 5601
curl localhost:9200/_cat/plugins                     # analysis-nori 확인
```

## 📝 과제 9 — 두 개 (마감이 다릅니다)

### 9-A. 검색 챌린지 [기본] (마감 12/03 (목) 13:30)

- [ ] `ProductSearchResponse` 응답 DTO 분리 (Document를 그대로 노출하지 않기)
- [ ] Gateway에서 `/products/search/internal/**` 외부 접근 차단
- [ ] 동의어 사전(`elasticsearch/product-settings.json`)에 본인이 정한 상품 도메인 단어 5개 추가 → 검색으로 확인
- [ ] **AI 수업 사전 준비**: Ollama 네이티브 설치, `ollama pull exaone3.5:2.4b`, `ollama pull bge-m3`, `ollama list` 캡처

### 9-B. ⭐ Transactional Outbox로 유실 원천 차단 (마감 12/07 (월) 13:30, 주말 포함)

> MSA ④에서 배운 Outbox를 **실제로 구현**하는 자리입니다.

- [ ] `product_outbox` 테이블 — 상품 변경과 **같은 트랜잭션**에 이벤트 저장
- [ ] `@Scheduled` 폴러가 미처리 이벤트를 ES에 색인하고 처리 마킹
- [ ] 장애 실험을 다시: ES 중단 → 상품 등록 → ES 재기동 → **재색인 API 없이** 검색에 나타나는지 검증 (영상 또는 캡처)

**도전 (택1 이상)**

- [ ] [정합성] 상품 삭제/판매종료 시 색인 처리 — delete vs filter 비교
- [ ] [운영] alias 기반 무중단 재색인 (`products_v2` → `_aliases` 원자적 스위치)
- [ ] [검색 품질] aggregation 가격대 패싯, `search_after` 페이지네이션

### 제출 방법 (공통)

1. 이 레포를 **fork** 한 뒤, 본인 fork에 `hw/search-1` 브랜치를 만들어 작업합니다.
2. 마감 전까지 **본인 fork의 브랜치 링크**(또는 PR 링크)를 과제 제출 채널에 올립니다.
3. 다음 날 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 `-done` 브랜치와 나란히 비교합니다.

> 막히면 `lectures/elasticsearch/step1-done` 브랜치와 `git diff`로 비교해 봐도 괜찮습니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
