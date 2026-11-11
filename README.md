# [MSA ①] 완성 — 단일 모놀리스 → common + 서비스 7개

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/msa/step1-done` (구현 후 · 완성 코드) |
| 강의일 | 과제 HW06-B 정답 · 11/25 (수) MSA ② 시작점 |
| 이동 | 이전: `lectures/msa/step1` · 다음: `lectures/msa/step2` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/msa/step1-done
> ```

## 이 브랜치에서 달라진 것

- 루트 `settings.gradle`이 `common` + `product / payment / user / seller / cart / settlement / order` 서비스 모듈로 구성
- 각 서비스가 자기 포트·자기 DB로 독립 기동
- 서비스 간 호출은 Port + HttpClient(RestClient) — 지금은 **주소가 하드코딩**돼 있음 (→ Step 2 Eureka의 출발점)

## 실행

```bash
./gradlew :product-service:bootRun     # 8081
./gradlew :payment-service:bootRun     # 8082
./gradlew :order-service:bootRun       # 8080
# 필요한 서비스를 각각 다른 터미널에서
```


## 📝 과제와의 관계

이 브랜치는 **과제 HW06-B** (`homework/msa-0`)의 정답 예시입니다. 과제 브랜치의 검증 테스트가 이 코드에서 통과합니다.


다음 수업은 `lectures/msa/step2`에서 시작합니다.
