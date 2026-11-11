# [정산 ①-2] Spring Batch — Chunk 지향 처리

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/settlement/step2` (구현 전 · 출발 코드) |
| 강의일 | Day 4 · 11/23 (월) |
| 이동 | 이전: `lectures/settlement/step1` · 다음: `lectures/settlement/step2-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/settlement/step2
> ```

## 이 브랜치는?

Step 1과 같은 출발 코드입니다. 배치 인프라(`spring-boot-starter-batch`, JobRepository 스키마, Job 자동실행 OFF)는 준비돼 있고,
**Job/Step은 지금부터 라이브 코딩으로** 채웁니다.

## 오늘 수업에서 하는 것 — Step 2

```
Reader(주문을 페이지로 읽기) → Processor(주문 → 정산 변환) → Writer(정산 적재)
     └──────── chunk(기본 1000)만큼 모아 한 트랜잭션 ────────┘
```

1. **2-1** `ItemReader` · `ItemProcessor` · `ItemWriter` 개념
2. **2-2** `JpaPagingItemReader`로 메모리를 안전하게 쓰는 정산 파이프라인 완성
3. 결론: 같은 100만 건 + `-Xmx1g`에서 **naive는 OOM, batch는 완주**

## 확인 포인트

- 콘솔의 `[mem:batch]` 막대가 **평평하게** 유지되는가 (naive의 우상향과 비교)
- `POST /settlements/batch` 응답의 read / written / elapsed / peakHeap
