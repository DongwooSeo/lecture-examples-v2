# [MSA ②-3] 완성 — Spring Cloud Config로 설정 중앙화

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/msa/step4-done` (구현 후 · 완성 코드) |
| 강의일 | Day 6 · 11/25 (수) |
| 이동 | 이전: `lectures/msa/step4` · 다음: `lectures/msa/step5` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/msa/step4-done
> ```

## 이 브랜치에서 달라진 것

- `config-server` 모듈 (:8888) — 설정 저장소: [`DongwooSeo/msa-config-repo`](https://github.com/DongwooSeo/msa-config-repo)
- 모든 서비스의 로컬 설정은 `spring.config.import: optional:configserver:http://localhost:8888`만 남김
- 포트·라우트·관리 엔드포인트는 config 레포의 `<서비스명>.yml`에서 배달

## 실행 순서

```bash
./gradlew :config-server:bootRun      # 1. 설정
./gradlew :discovery-server:bootRun   # 2. 디스커버리
./gradlew :gateway-server:bootRun     # 3. 게이트웨이 (:8000)
./gradlew :order-service:bootRun      # 4. 서비스들
```


## 📝 과제와의 관계

이 브랜치는 **과제 HW07** (`homework/msa-1`)의 정답 예시입니다. 과제 브랜치의 검증 테스트가 이 코드에서 통과합니다.


다음 수업은 `lectures/msa/step5`에서 시작합니다.
