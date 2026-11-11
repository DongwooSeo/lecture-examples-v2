# [DDD ③-1] 완성 — 도메인별 4계층 분리 + DIP

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/ddd/step4-done` (구현 후 · 완성 코드) |
| 강의일 | Day 3 · 11/19 (목) |
| 이동 | 이전: `lectures/ddd/step4` · 다음: `lectures/ddd/step5` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/ddd/step4-done
> ```

## 이 브랜치에서 달라진 것

- 6개 도메인 모두 `presentation / application / domain / infrastructure` 4계층으로 분리
- Repository 인터페이스는 domain, JPA 구현(`*RepositoryAdapter`)은 infrastructure → **DIP**
- 공통 응답 `common/response/ApiResponse`, 공통 예외 `common/exception/*`
- 단위·슬라이스 테스트 추가

| 테스트 | 검증하는 것 |
| --- | --- |
| `MoneyTest`, `OrderTest` | 금액 VO, 주문 애그리거트 불변식 |
| `CartTest`, `ProductTest`, `PaymentTest`, `SellerTest` | 각 도메인 규칙 |
| `OrderServiceTest` | 유스케이스 흐름 |
| `OrderControllerTest` | API 응답 형식 |

## 확인

```bash
./gradlew test
```

## 📝 과제와의 관계

이 브랜치는 **과제 3 (나머지 도메인 4계층 + 공통 응답)**의 비교 기준(정답 예시)입니다. 과제 내용은 `lectures/ddd/step4` 브랜치 README를 확인하세요.


다음 수업은 `lectures/ddd/step5`에서 시작합니다.
