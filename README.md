# 📝 과제 HW10 — 10개 서비스 Dockerfile + Compose 로 전체 기동

| 항목 | 내용 |
| --- | --- |
| 마감 | **12/01 (화) 13:30** |
| 유형 | 복습 · 예상 2.5시간 |
| 시작 브랜치 | `homework/infra-1` (이 브랜치) |
| 정답 예시 | `homework/infra-1-solution` |
| 이어지는 수업 | Day 9 · CI/CD ① 컨테이너와 쿠버네티스 (11/30) |

## 왜 하나요?

수업에서 `order-service` 를 이미지로 만들고, 인프라 3종 + 서비스 3개를 `docker-compose.yml` 로 띄웠습니다.
나머지 서비스까지 담으면 **명령어 한 줄로 MSA 전체가 뜨는** 온보딩이 완성됩니다.

## 할 일

**필수**

- [ ] `user-service`(8083) · `seller-service`(8084) · `cart-service`(8085) · `settlement-service`(8086) Dockerfile
- [ ] `docker-compose.yml` 에 4개 서비스 추가 — config-server · Eureka 주소를 **컨테이너 이름**으로 (`x-client-env` 참고)
- [ ] `./gradlew bootJar && docker compose up -d --build` 후 Gateway(8000) 로 각 서비스 호출 성공

**도전**

- [ ] `healthcheck` + `depends_on: condition: service_healthy` 로 "떠 있음"이 아니라 "준비됨" 뒤에 클라이언트 기동
  - JRE 이미지에는 curl 이 없습니다. `bash -c '</dev/tcp/localhost/8888'` 로 포트를 확인해 보세요
- [ ] YAML 앵커(`&`, `*`, `<<:`)로 서비스 10개의 반복 설정 줄이기

## 완료 기준 — 검증 스크립트

```bash
./scripts/verify-infra-1.sh          # Dockerfile 10개, compose 서비스 10개 검사
./scripts/verify-infra-1.sh --run    # 빌드 → 기동 → Gateway 경유 호출까지
```

## 제출 방법

1. 이 레포를 **fork** 하고, 본인 fork 에서 이 브랜치를 기준으로 `hw/infra-1` 브랜치를 만듭니다.
   ```bash
   git fetch upstream && git checkout -b hw/infra-1 upstream/homework/infra-1
   ```
2. **마감(12/01 (화) 13:30) 전까지** 본인 fork 의 브랜치(또는 PR) 링크를 과제 제출 채널에 올립니다.
3. 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 정답 브랜치와 나란히 비교합니다.

> 막히면 정답 브랜치 `homework/infra-1-solution` 와 `git diff` 로 비교해 봐도 됩니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
