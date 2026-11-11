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

## 📝 과제 8 — cart-service Feign 전환 + 실패 재현 + Kafka 예열 (마감 11/27 (금) 13:30)

**필수**

- [ ] `cart-service`의 `ProductHttpClient`(RestClient)를 **FeignClient**로 교체 — 기존 서킷 브레이커·Fallback 동작은 유지
- [ ] 위 동기 호출 실패 3케이스를 재현하는 `sync-failures.http` 작성, 각 케이스 결과(재고·주문 상태)를 표로 정리

**예열 (필수, 20분)** — 내일 MSA ④ Kafka 수업 준비

- [ ] Docker로 Kafka 단일 브로커 기동 후, 콘솔 producer로 보낸 메시지를 콘솔 consumer로 받는 화면 캡처

**도전**

- [ ] Feign에 Retry를 붙이되, 400·404·422는 재시도하지 않도록 `ErrorDecoder`로 구분

### 제출 방법 (공통)

1. 이 레포를 **fork** 한 뒤, 본인 fork에 `hw/msa-2` 브랜치를 만들어 작업합니다.
2. 마감 전까지 **본인 fork의 브랜치 링크**(또는 PR 링크)를 과제 제출 채널에 올립니다.
3. 다음 날 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 `-done` 브랜치와 나란히 비교합니다.

> 막히면 `lectures/msa/step6-done` 브랜치와 `git diff`로 비교해 봐도 괜찮습니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
