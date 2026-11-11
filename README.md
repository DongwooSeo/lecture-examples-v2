# 📝 과제 HW08 — cart-service Feign 전환 + 동기 호출 실패 재현 + Kafka 예열

| 항목 | 내용 |
| --- | --- |
| 마감 | **11/27 (금) 13:30** |
| 유형 | 복습+예열 · 예상 2.5시간 |
| 시작 브랜치 | `homework/msa-2` (이 브랜치) |
| 정답 예시 | `homework/msa-2-solution` |
| 이어지는 수업 | Day 7 · MSA ③ Resilience4j · OpenFeign (11/26) |

## 왜 하나요?

수업에서 `order-service` 의 원격 호출을 OpenFeign 으로 바꿨습니다. `cart-service` 는 아직 RestClient 손코딩입니다.
그리고 내일 MSA ④ 는 "동기 호출이 왜 위험한가"에서 출발해 Kafka 로 넘어갑니다.

## 할 일

**A. cart-service Feign 전환 (필수)**

- [ ] `spring-cloud-starter-openfeign` 의존성, `@EnableFeignClients`
- [ ] `@FeignClient(name = "product-service")` 인터페이스로 `GET /products/{id}` 호출
- [ ] `ProductHttpClient` 는 Feign 을 쓰되 **서킷 브레이커 + 낙관적 Fallback 은 그대로 유지**
- [ ] 필요 없어진 RestClient 설정(`CartClientConfig`) 정리

**B. 동기 호출 실패 3케이스 재현 (필수)**

`sync-failures.http` 를 만들고 각 케이스의 결과(재고 · 주문 상태 · 응답 코드)를 표로 정리하세요.

| 케이스 | 재현 방법 (예) |
| --- | --- |
| 1. 상대 서비스 다운 | product-service 를 끄고 주문 |
| 2. 타임아웃 (성공했는데 실패로 아는 상황) | `FakePaymentGateway` 지연을 타임아웃보다 길게 |
| 3. 호출 성공 후 후속 로직 실패 | 재고 차감 성공 → 결제 실패 |

**C. Kafka 예열 (필수, 20분)**

```bash
docker run -d --name kafka-warmup -p 9092:9092 apache/kafka:3.9.1
docker exec -it kafka-warmup /opt/kafka/bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic hello
docker exec -it kafka-warmup /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic hello --from-beginning
```

producer 에 보낸 메시지가 consumer 에 찍히는 화면을 캡처하세요.

**도전**

- [ ] Feign 에 Retry 를 붙이되 400 · 404 · 422 는 재시도하지 않도록 `ErrorDecoder` 로 구분

## 완료 기준 — 검증 테스트

```bash
./gradlew :cart-service:test --tests '*CartFeignMigrationTest*'
```

cart-service 소스에 RestClient import 가 0개이고, `@FeignClient(name = "product-service")` · `@EnableFeignClients` · 서킷 브레이커 호출이 있는지 검사합니다.

## 제출 방법

1. 이 레포를 **fork** 하고, 본인 fork 에서 이 브랜치를 기준으로 `hw/msa-2` 브랜치를 만듭니다.
   ```bash
   git fetch upstream && git checkout -b hw/msa-2 upstream/homework/msa-2
   ```
2. **마감(11/27 (금) 13:30) 전까지** 본인 fork 의 브랜치(또는 PR) 링크를 과제 제출 채널에 올립니다.
3. 14:00 수업은 **과제 리뷰 15분**으로 시작합니다. 2~3명의 코드를 정답 브랜치와 나란히 비교합니다.

> 막히면 정답 브랜치 `homework/msa-2-solution` 와 `git diff` 로 비교해 봐도 됩니다. 단, **왜 그렇게 바뀌었는지 설명할 수 있어야** 합니다.
