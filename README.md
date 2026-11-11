# [검색 ②] 완성 — Elasticsearch 검색 + 색인 파이프라인

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/elasticsearch/step1-done` (구현 후 · 완성 코드) |
| 강의일 | Day 11 · 12/02 (수) |
| 이동 | 이전: `lectures/elasticsearch/step1` · 다음: `lectures/ai/step1` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/elasticsearch/step1-done
> ```

## 이 브랜치에서 달라진 것

| 구성 | 위치 | 설명 |
| --- | --- | --- |
| 인프라 | `infrastructure/docker-compose.yml`, `infrastructure/elasticsearch/Dockerfile` | nori 포함 ES 9200 + Kibana 5601 |
| 인덱스 설정 | `product-service/src/main/resources/elasticsearch/product-settings.json` | 동의어 · 자동완성 분석기 |
| Read model | `product/infrastructure/search/ProductDocument` | `_id = productId` |
| 색인 | `ProductIndexer`, `application/ProductSearchSyncService` | 커밋 후 이벤트(`ProductChangedEvent`)로 색인 |
| 검색 | `application/ProductSearchService` | multi_match + 필터 + 랭킹 |
| API | `ProductSearchController` | 아래 참고 |

```http
GET  http://localhost:8081/products/search?keyword=노트북&minPrice=100000
GET  http://localhost:8081/products/search/autocomplete?prefix=노트
POST http://localhost:8081/products/search/internal/reindex
```

## 장애 실험 다시 해보기

```bash
docker stop lecture-es
# 상품 등록 → 성공 / 검색 → 없음
docker start lecture-es
curl -X POST localhost:8081/products/search/internal/reindex   # 복구
```


## 📝 과제와의 관계

- **HW12** (검색 챌린지 [기본])는 이 브랜치에서 시작합니다. 정답 브랜치는 없고 14:00 리뷰에서 함께 봅니다.
- **HW14** ⭐ Outbox 는 `homework/search-outbox` 에서 시작하고, 정답 예시는 `homework/search-outbox-solution` 에 있습니다.

다음 수업은 `lectures/ai/step1`에서 시작합니다.
