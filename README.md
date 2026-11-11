# 📝 과제 HW03 — 6개 도메인 4계층 + DIP + 공통 응답

| 항목 | 내용 |
| --- | --- |
| 마감 | **11/20 (금) 13:30** |
| 유형 | 복습 · 예상 3시간 |
| 시작 브랜치 | `homework/ddd-3` (이 브랜치) |
| 정답 예시 | `lectures/ddd/step4-done` |
| 이어지는 수업 | Day 3 · DDD ③ 4계층 (11/19) |

## 왜 하나요?

수업에서 **주문** 도메인을 4계층으로 나눴습니다. 나머지 5개 도메인도 같은 구조로 나누고,
모든 API 의 응답 모양을 하나로 통일합니다. 이 구조가 다음 주 MSA 분리의 밑바탕입니다.

```
<domain>/
├── presentation/   ← Controller, 요청·응답 DTO
├── application/    ← 유스케이스 서비스, 커맨드·결과 DTO
├── domain/         ← Entity, VO, Repository 인터페이스  (스프링·웹 import 금지)
└── infrastructure/ ← JPA 구현 (*JpaRepository, *RepositoryAdapter)
```

## 할 일

**필수**

- [ ] cart · order · payment · product · seller · user **6개 모두** 4계층으로 (주문은 수업 코드를 이어서)
- [ ] 각 도메인의 `Repository` 는 domain 에 인터페이스, infrastructure 에 구현 (**DIP**)
- [ ] 도메인 루트 패키지에 계층 밖으로 남은 클래스 0개
- [ ] `common/response/ApiResponse<T>` — `{ "success": true, "data": … }` / `{ "success": false, "error": { "code", "message" } }`
- [ ] 모든 컨트롤러 메서드가 `ApiResponse` 를 반환
- [ ] `common/exception/GlobalExceptionHandler` (`@RestControllerAdvice`) 로 예외 → 실패 응답 변환

**도전**

- [ ] 정답 브랜치의 `OrderServiceTest`, `OrderControllerTest` 를 가져와 본인 코드에서 통과시키기

## 완료 기준 — 검증 테스트

```bash
./gradlew test --tests '*homework*'
```

| 테스트 | 검사 내용 |
| --- | --- |
| `LayeredArchitectureTest` | 6개 도메인 × 4계층 패키지 존재, 계층 밖 클래스 0개, domain 계층의 금지 import(스프링·서블릿·다른 계층) 0개 |
| `ApiResponseConventionTest` | `ApiResponse` 클래스 존재, 모든 `@RestController` 메서드가 `ApiResponse` 반환, `@RestControllerAdvice` 존재 |

ArchUnit 같은 라이브러리 없이, 소스 코드와 리플렉션으로 검사합니다. 실패 메시지에 고쳐야 할 파일 목록이 나옵니다.

## 제출 방법

1. 이 레포를 **fork** 하고, 본인 fork 에서 이 브랜치를 기준으로 `hw/ddd-3` 브랜치를 만듭니다.
   ```bash
   git fetch upstream && git checkout -b hw/ddd-3 upstream/homework/ddd-3
   ```
2. **마감(11/20 (금) 13:30) 전까지** 본인 fork 의 브랜치(또는 PR) 링크를 과제 제출 채널에 올립니다.
3. 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 정답 브랜치와 나란히 비교합니다.

> 막히면 정답 브랜치 `lectures/ddd/step4-done` 와 `git diff` 로 비교해 봐도 됩니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
