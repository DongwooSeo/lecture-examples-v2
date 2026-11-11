# ✅ 과제 HW08 (A) 정답 예시

| 항목 | 내용 |
| --- | --- |
| 브랜치 | `homework/msa-2-solution` |
| 과제 | `homework/msa-2` 브랜치 README |

> 정답은 하나가 아닙니다. 이 브랜치는 **검증 테스트를 통과하는 한 가지 방법**입니다.
> 본인 코드와 비교할 때는 `git diff homework/msa-2 homework/msa-2-solution` 로 무엇이 바뀌었는지부터 보세요.

## 바뀐 것

- `cart-service/.../client/ProductFeignClient.java` — `@FeignClient(name = "product-service")`
- `ProductHttpClient` — RestClient 대신 Feign 호출, 서킷 브레이커와 낙관적 Fallback 은 그대로
- `CartClientConfig` 삭제, `@EnableFeignClients`, openfeign 의존성 추가

## 확인

과제 브랜치의 완료 기준 명령을 그대로 실행하면 모두 통과합니다.
