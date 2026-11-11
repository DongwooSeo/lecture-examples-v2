# [MSA ①] 왜 MSA인가 — 정산까지 완성된 모놀리식

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/msa/step1` (구현 전 · 출발 코드) |
| 강의일 | Day 5 · 11/24 (화) 18:00~19:00 |
| 이동 | 이전: `lectures/settlement/step4-done` · 다음: `lectures/msa/step1-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/msa/step1
> ```

## 이 브랜치는?

DDD → 정산까지 완성된 **하나의 모놀리식**입니다. MSA ① 시간에는 코드를 고치지 않고, 이 프로젝트를 놓고 이야기합니다.

- 모놀리식의 성장통: 배포 병목, 장애 전파, 팀 확장의 한계
- MSA 가 해결하는 것과 새로 떠안는 것 — 트레이드오프
- 전환의 전제 조건: **도메인 경계(DDD)가 먼저다** — 지난주에 끊어 둔 경계가 그대로 서비스 경계가 됩니다

목표 구조 (`lectures/msa/step1-done`):

```
common/              ← ApiResponse, 공통 예외
product-service/     :8081      payment-service/  :8082
user-service/        :8083      seller-service/   :8084
cart-service/        :8085      settlement-service/ :8086
order-service/       :8080
```

## 📝 오늘의 과제 (내일 오전)

| 과제 | 마감 | 시작 브랜치 | 검증 |
| --- | --- | --- | --- |
| **HW06-B** 멀티 모듈 분리 — product · payment · order 이관 | 11/25 (수) 13:30 | `homework/msa-0` | 브랜치 README 의 완료 기준 |

과제 내용 · 완료 기준 · 제출 방법은 **시작 브랜치의 README** 에 있습니다.
```bash
git fetch origin && git checkout homework/msa-0
```

> 분량을 줄이려고 user · seller · cart · settlement 4개는 미리 옮겨 두었습니다. product · payment · order 3개를 직접 옮깁니다.
