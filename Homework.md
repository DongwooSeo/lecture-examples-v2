애그리거트(Aggregate) : 서로 밀접하게 관련된 객체들을 하나의 관리 단위로 묶는 것
- Cart: 애그리거트 루트(Aggregate Root), 장바구니 항목을 관리하는 책임자
- CartItem의 생성, 수정, 삭제는 Cart를 통해 수행
CartItem을 외부에서 마음대로 변경하지 못하게 하고, Cart를 통해서만 관리하도록 만드는 것. 

불변식(Invariant) : 객체가 어떤 작업을 수행하더라도 항상 지켜야 하는 규칙

ex) 장바구니의 불변식
    - 상품 수량은 반드시 1 이상 
    - 같은 상품은 장바구니에 한 번만 존재
    - 존재하지 않는 상품의 수량을 변경하거나 삭제할 수 없음

캡슐화(Encapsulation) : 객체 내부의 데이터를 외부에서 직접 조작하지 못하게 하고, 정해진 메서드를 통해서만 
변경하도록 만드는 것

``` java
cart.addItem(CartItem.create(productId, qty))
```
before 
: 외부에서 CartItem을 만들어 전달하는 구조 

after
: 데이터를 변경하는 권한과 규칙을 Cart 내부로 모으는 것



