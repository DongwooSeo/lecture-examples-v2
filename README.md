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

## 📝 과제 7 — Spring Cloud 3종을 전체 서비스에 (마감 11/26 (목) 13:30)

> 수업에서는 order · product에 먼저 적용했습니다. **본인 브랜치**에서 나머지 서비스까지 완성하세요.

**필수**

- [ ] 7개 서비스 + gateway가 모두 Eureka에 등록
- [ ] Gateway 라우트로 7개 서비스 전체 API 호출 가능 — 서비스별 1개 이상 요청을 담은 `gateway.http` 작성
- [ ] [`msa-config-repo`](https://github.com/DongwooSeo/msa-config-repo)를 **본인 계정으로 fork** → `config-server`의 `uri`를 본인 fork로 변경
- [ ] 본인 config 레포에서 값 하나(예: 로그 레벨)를 바꾸고, 서비스 재시작으로 반영되는 것 확인 (스크린샷)

**도전**

- [ ] `/actuator/refresh`로 **재시작 없이** 반영해 보기 (`@RefreshScope`)
- [ ] product-service를 포트 2개로 띄워 Gateway 호출이 번갈아 가는지(로드밸런싱) 확인

### 제출 방법 (공통)

1. 이 레포를 **fork** 한 뒤, 본인 fork에 `hw/msa-1` 브랜치를 만들어 작업합니다.
2. 마감 전까지 **본인 fork의 브랜치 링크**(또는 PR 링크)를 과제 제출 채널에 올립니다.
3. 다음 날 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 `-done` 브랜치와 나란히 비교합니다.

> 막히면 `lectures/msa/step4-done` 브랜치와 `git diff`로 비교해 봐도 괜찮습니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
