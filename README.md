# 📝 과제 HW07 — Eureka · Config 를 나머지 5개 서비스에

| 항목 | 내용 |
| --- | --- |
| 마감 | **11/26 (목) 13:30** |
| 유형 | 복습 · 예상 2.5시간 |
| 시작 브랜치 | `homework/msa-1` (이 브랜치) |
| 정답 예시 | `lectures/msa/step4-done` |
| 이어지는 수업 | Day 6 · MSA ② Eureka · Gateway · Config (11/25) |

## 왜 하나요?

수업에서는 `order` · `product` 와 인프라 서버 3종(discovery · gateway · config)을 연결했습니다.
이 브랜치는 **payment · user · seller · cart · settlement 5개 서비스의 연결을 되돌려 둔** 상태입니다 — 아직 하드코딩된 주소로 호출하고, 설정도 로컬 파일에 있습니다.

## 할 일

**필수**

- [ ] 5개 서비스에 Eureka Client + Config Client 의존성 추가 (`order-service/build.gradle` 참고)
- [ ] 로컬 설정은 "열쇠"만 남기기 — `application.yml` 에 `spring.application.name`, `spring.config.import: optional:configserver:http://localhost:8888`
- [ ] `cart-service` 의 상품 호출을 `@LoadBalanced` RestClient + `http://product-service` (이름 기반)로
- [ ] [`msa-config-repo`](https://github.com/DongwooSeo/msa-config-repo) 를 **본인 계정으로 fork** → `config-server` 의 `uri` 를 본인 fork 로 변경
- [ ] 본인 config 레포에서 값 하나(예: 로그 레벨)를 바꾸고, 서비스 재시작으로 반영되는 것 확인 (스크린샷)

**도전**

- [ ] `/actuator/refresh` 로 **재시작 없이** 반영 (`@RefreshScope`)
- [ ] product-service 를 포트 2개로 띄워 Gateway 호출이 번갈아 가는지(로드밸런싱) 확인

## 완료 기준

1. 기동 순서: `config-server` → `discovery-server` → `gateway-server` → 서비스 7개
2. `http://localhost:8761` 에 **서비스 7개 + gateway = 8개**가 UP
3. 아래 요청을 모두 Gateway(8000) 경유로 성공시키는 `gateway.http` 작성

```http
GET  http://localhost:8000/products/1
GET  http://localhost:8000/users/1
GET  http://localhost:8000/sellers/1
GET  http://localhost:8000/users/1/cart
POST http://localhost:8000/orders
GET  http://localhost:8000/settlements/status
```

> 404 가 나면 라우트(config 레포의 `gateway-server.yml`)와 `spring.application.name` 을 먼저 의심합니다.

## 제출 방법

1. 이 레포를 **fork** 하고, 본인 fork 에서 이 브랜치를 기준으로 `hw/msa-1` 브랜치를 만듭니다.
   ```bash
   git fetch upstream && git checkout -b hw/msa-1 upstream/homework/msa-1
   ```
2. **마감(11/26 (목) 13:30) 전까지** 본인 fork 의 브랜치(또는 PR) 링크를 과제 제출 채널에 올립니다.
3. 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 정답 브랜치와 나란히 비교합니다.

> 막히면 정답 브랜치 `lectures/msa/step4-done` 와 `git diff` 로 비교해 봐도 됩니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
