package com.growmighty.lectures.firstday.tangledmonolith.seller;

//import com.growmighty.lectures.firstday.tangledmonolith.product.Product;
//import com.growmighty.lectures.firstday.tangledmonolith.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//import java.util.ArrayList;
//import java.util.List;
@Getter
@Entity
@Table(name = "sellers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToMany(mappedBy = "seller")
//    private List<Product> products = new ArrayList<>();
//    판매자의 상품 객체 목록 제거 :

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
    private Long userId;

    public static Seller create(Long userId) {
        Seller seller = new Seller();
        seller.userId = userId;
        
        return seller;
    }
}
