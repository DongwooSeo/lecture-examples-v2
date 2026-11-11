# [MSA ③-2] OpenFeign — 계약서만 쓰세요, 통역은 스프링이 합니다

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/msa/step6` (구현 전 · 출발 코드) |
| 강의일 | Day 7 · 11/26 (목) 16:40~19:00 |
| 이동 | 이전: `lectures/msa/step5-done` · 다음: `lectures/msa/step6-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/msa/step6
> ```

## 이 브랜치는?

서비스 간 호출을 RestClient로 **직접** 작성하고 있습니다 (URL 조립, 응답 파싱, 예외 변환이 모두 손코딩).

## 오늘 수업에서 하는 것

- **Step 6** order-service의 원격 호출을 **선언적 HTTP 클라이언트(OpenFeign)**로 교체 (Load Balancer 내장 동작 확인)
- 라이브 데모 — 동기 호출이 만드는 불일치 3케이스
  1. 주문 → 상품 호출 시 상품 서비스가 죽어 있다면?
  2. 응답이 **타임아웃**이라면? (성공했는데 실패로 아는 상황)
  3. 호출은 성공했는데 그 뒤 주문 로직에서 실패한다면?
- 개념: MSA 인증/인가 — Gateway에서 토큰 검증, 서비스 간 사용자 정보 전파

## 📝 오늘의 과제 (내일 오전)

| 과제 | 마감 | 시작 브랜치 | 검증 |
| --- | --- | --- | --- |
| **HW08** cart-service Feign 전환 + 동기 호출 실패 재현 + Kafka 예열 | 11/27 (금) 13:30 | `homework/msa-2` | 브랜치 README 의 완료 기준 |

과제 내용 · 완료 기준 · 제출 방법은 **시작 브랜치의 README** 에 있습니다.
```bash
git fetch origin && git checkout homework/msa-2
```
