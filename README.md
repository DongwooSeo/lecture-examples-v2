# [MSA ②-3] 중앙 설정 관리 — 재배포 없이 바꿉니다

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `lectures/msa/step4` (구현 전 · 출발 코드) |
| 강의일 | Day 6 · 11/25 (수) ~19:00 |
| 이동 | 이전: `lectures/msa/step3-done` · 다음: `lectures/msa/step4-done` |

> **브랜치 규칙** — `stepN` = 구현 **전** 출발 코드, `stepN-done` = 구현 **후** 완성 코드.
> 수업은 `stepN`에서 시작하고, 막히면 언제든 `stepN-done`으로 체크아웃해 다음 단계에 합류하세요.
> ```bash
> git fetch origin && git checkout lectures/msa/step4
> ```

## 이 브랜치는?

설정(포트, 라우트, 클라이언트 주소 …)이 서비스마다 `application.*`에 **흩어져** 있습니다. 하나 바꾸려면 해당 서비스를 다시 빌드·배포해야 합니다.

## Step 4에서 할 것

- [ ] `config-server` 모듈 추가 (:8888), 설정 저장소는 Git 레포
- [ ] 각 서비스의 로컬 설정은 "열쇠"(`spring.application.name`, `spring.config.import`)만 남기기
- [ ] 설정을 바꾸고 서비스 재시작만으로 반영되는 것 확인

## 📝 오늘의 과제 (내일 오전)

| 과제 | 마감 | 시작 브랜치 | 검증 |
| --- | --- | --- | --- |
| **HW07** Eureka · Config 를 나머지 5개 서비스에 | 11/26 (목) 13:30 | `homework/msa-1` | 브랜치 README 의 완료 기준 |

과제 내용 · 완료 기준 · 제출 방법은 **시작 브랜치의 README** 에 있습니다.
```bash
git fetch origin && git checkout homework/msa-1
```
