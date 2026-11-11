# [정산 ①-1] 배치가 필요한 이유 — 100만 건으로 OOM 만들기

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/settlement/step1` (구현 전 · 출발 코드) |
| 강의일 | Day 4 · 11/23 (월) 15:00~ |
| 이동 | 이전: `lectures/ddd/step5-done` · 다음: `lectures/settlement/step2` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/settlement/step1
> ```

## 이 브랜치는?

DDD 완성 코드 위에 **정산 도메인의 출발점**이 세팅돼 있습니다. 배치(Job/Step)는 아직 없고, 일부러 만든 **안티패턴 서비스**만 있습니다.

| 영역 | 위치 | 설명 |
| --- | --- | --- |
| 정산 엔티티 | `settlement/domain/Settlement` | **1원의 무결성**(수수료 + 지급액 = 주문금액)을 `verifyIntegrity()`로 강제, `order_id` 유니크 |
| 안티패턴 서비스 | `settlement/application/NaiveSettlementService` | `findAll()` 전량 적재 → OOM |
| 힙 모니터 | `settlement/support/HeapMonitor` | 0.5초마다 `[mem]` 막대로 힙 사용량 로그 |
| 대용량 시더 | `settlement/support/SettlementDataSeeder` | 주문/결제 N건 배치 INSERT |
| API | `SettlementController` | `POST /settlements/naive[?limit=N]`, `GET /settlements/status`, `DELETE /settlements` |

강사용 상세 진행 가이드: [`SETTLEMENT_SESSION.md`](SETTLEMENT_SESSION.md)

## 오늘 수업에서 하는 것

- 정산 도메인의 특수성 — **1원의 무결성** (어디서 반올림하고 어디서 나머지를 취하나)
- **Step 1** 배치가 필요한 이유: 데이터가 늘수록 시간·메모리가 선형 증가 → `findAll()` 한 줄로 OOM

## 실행

IDE에서 `main()` 실행 — VM options:

```
-Xmx1g -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./oom.hprof
```

Program arguments:

```
--settlement.seed.enabled=true --settlement.seed.count=1000000 --spring.jpa.show-sql=false --logging.level.org.hibernate.orm.jdbc.bind=off
```

```http
GET  http://localhost:8080/settlements/status
POST http://localhost:8080/settlements/naive?limit=100000
POST http://localhost:8080/settlements/naive          # limit 없음 = findAll → OOM
```

## 📝 사전 과제 — OOM 직접 재현하기 (마감 11/23 (월) 13:30)

> 수업 전에 **직접 터뜨려 보고** 오면, 오후의 Chunk 처리가 왜 필요한지 몸으로 이해됩니다.

- [ ] 위 설정으로 기동해 `limit=100000 / 300000 / 500000`을 차례로 호출 (사이사이 `DELETE /settlements`)
- [ ] 응답의 `elapsedMs`, `peakHeapMb`를 표로 정리
- [ ] `limit` 없이 호출해 OOM을 발생시키고 `oom.hprof` 생성 확인
- [ ] (도전) IntelliJ로 `oom.hprof`를 열어 가장 많은 객체 3종과 개수를 캡처

### 제출 방법 (공통)

1. 이 레포를 **fork** 한 뒤, 본인 fork에 `hw/settlement-0` 브랜치를 만들어 작업합니다.
2. 마감 전까지 **본인 fork의 브랜치 링크**(또는 PR 링크)를 과제 제출 채널에 올립니다.
3. 다음 날 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 `-done` 브랜치와 나란히 비교합니다.

> 막히면 `lectures/settlement/step2-done` 브랜치와 `git diff`로 비교해 봐도 괜찮습니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
