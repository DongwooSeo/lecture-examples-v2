# 📝 과제 HW11 — 전 서비스 CI — 재사용 워크플로 + 역색인 예열

| 항목 | 내용 |
| --- | --- |
| 마감 | **12/02 (수) 13:30** |
| 유형 | 복습+예열 · 예상 2.5시간 |
| 시작 브랜치 | `homework/infra-2` (이 브랜치) |
| 정답 예시 | `homework/infra-2-solution` |
| 이어지는 수업 | Day 10 · CI/CD ② GitHub Actions (12/01) |

## 왜 하나요?

수업에서 `order-service` 하나에 "push → 테스트 → jar → 이미지 → GHCR" 파이프라인을 붙였습니다.
같은 파일을 서비스 10개에 복붙하면 한 줄 고칠 때 10곳을 고쳐야 합니다. **재사용 워크플로**로 한 곳에 모읍니다.

## 할 일

**A. CI (필수)**

- [ ] `.github/workflows/_service-ci.yml` — `on: workflow_call` + `inputs.module` 로 공통 단계 추출
- [ ] 서비스 10개 각각 `.github/workflows/<서비스>.yml` — `paths` 필터에 `"<서비스>/**"` + `common/**`, 공통 워크플로를 `uses:` 로 호출
- [ ] 본인 fork 에 push 해서 Actions 탭에 녹색 체크, GHCR(Packages)에 이미지가 올라온 것 캡처

**B. 역색인 예열 (필수, 20분)** — 내일 검색 수업 준비

아래 상품 3개로 **역색인 표**(단어 → 상품 ID 목록)를 손으로 그려 오세요. 띄어쓰기 단위로 자르면 무엇이 문제인지도 한 줄로.

```
1: 삼성 노트북 갤럭시북4 프로
2: LG 그램 노트북 17인치
3: 노트북 파우치 15인치
```

**도전**

- [ ] `pull_request` 에서는 테스트만, `main` push 에서만 이미지 push 되는지 확인
- [ ] `concurrency` 로 같은 브랜치의 이전 실행 취소

## 완료 기준 — 검증 스크립트

```bash
./scripts/verify-infra-2.sh
```

재사용 워크플로 존재, 서비스 10개 워크플로 존재, 각 워크플로의 `paths` 필터와 `uses:` 호출을 검사합니다.

## 제출 방법

1. 이 레포를 **fork** 하고, 본인 fork 에서 이 브랜치를 기준으로 `hw/infra-2` 브랜치를 만듭니다.
   ```bash
   git fetch upstream && git checkout -b hw/infra-2 upstream/homework/infra-2
   ```
2. **마감(12/02 (수) 13:30) 전까지** 본인 fork 의 브랜치(또는 PR) 링크를 과제 제출 채널에 올립니다.
3. 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 정답 브랜치와 나란히 비교합니다.

> 막히면 정답 브랜치 `homework/infra-2-solution` 와 `git diff` 로 비교해 봐도 됩니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
