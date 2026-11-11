# [정산 ②] 성능의 한계 돌파 — 멀티스레드의 함정과 Partitioning

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/settlement/step4` (구현 전 · 출발 코드) |
| 강의일 | Day 5 · 11/24 (화) 14:00~18:00 |
| 이동 | 이전: `lectures/settlement/step3-done` · 다음: `lectures/settlement/step4-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/settlement/step4
> ```

## 이 브랜치는?

멱등·재시작까지 갖춘 정산 배치입니다. 남은 문제는 **속도**입니다. 단일 스레드로는 마감 시간을 못 맞춥니다.

## 오늘 수업에서 하는 것 — Step 4

> 느리다 → 스레드 늘림 → 풀이 터진다(고침) → 고쳤는데도 안 빨라진다(직접 측정) → 읽기가 한 줄로 흐른다 → 재시작까지 잃는다 → 그래서 파티셔닝.

1. **4-1** 멀티스레드 Step → HikariCP 풀 고갈(`Connection is not available`) → 풀 튜닝
2. **4-2** 스레드 1 → 2 → 4 → 8 직접 측정 — Reader가 하나라 **읽기 깔때기**
3. **4-3** 멀티스레드 Step은 `saveState=false` → 강제 종료 후 재시작이 처음부터
4. **4-4** **Partitioning** — 파티션마다 전용 Reader, 파티션별 재시작 보존
5. 개념 20분: Driving Table, Cursor vs Paging, Bulk Insert, 인덱스 전략

## 📝 과제 HW06 — 측정 보고서 + 멀티 모듈 분리 (마감 11/25 (수) 13:30)

**A. 측정 보고서 (필수, 30분)** — `lectures/settlement/step4-done` 기준

- [ ] 시드 2만, chunk 200에서 `multi-threaded?threads=1/2/4/8`, `partitioned?gridSize=1/2/4/8` 실행
- [ ] elapsedMs와 배속을 한 표에 정리하고, 두 방식의 차이를 3줄로 설명
- [ ] gridSize를 16으로 올려도 빨라지지 않는다면, 진짜 병목이 무엇인지 추측

**B. 멀티 모듈 분리 (필수, 2.5시간)** — 내일 MSA 수업의 출발점

- [ ] 과제 브랜치 `homework/msa-0` 의 README 를 진행하세요. (product · payment · order 3개 서비스를 모듈로 이관)

### 제출 방법 (공통)

1. 이 레포를 **fork** 한 뒤, 본인 fork에 `hw/settlement-2` 브랜치를 만들어 작업합니다.
2. 마감 전까지 **본인 fork의 브랜치 링크**(또는 PR 링크)를 과제 제출 채널에 올립니다.
3. 다음 날 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 `-done` 브랜치와 나란히 비교합니다.

> 막히면 `lectures/settlement/step4-done` 브랜치와 `git diff`로 비교해 봐도 괜찮습니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
