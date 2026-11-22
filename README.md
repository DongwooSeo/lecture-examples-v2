**도전**

- [O] 한 장바구니에 담을 수 있는 상품 종류 상한(예: 50종) 규칙 추가 + 테스트
  - Cart.java 내 CAPACITY 상수 선언(현재 50), CartAggregateTest.java 장바구니_상한 Test
- [O] "이 규칙은 Entity 에 둘까, Domain Service 에 둘까?" 판단 근거를 PR 본문에 3줄로
  - 특정 엔티티에 넣기 애매한 경우, 즉 여러 엔티티가 책임지는 경우 도메인 서비스 계층에서 처리
