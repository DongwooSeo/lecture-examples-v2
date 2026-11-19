package com.growmighty.lectures.firstday.tangledmonolith.seller;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sellers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    // Seller가 물건을 팔고 있지 않거나, 여러 개를 팔고 있을 수 있음
    // List 형태로 Product를 저장하는 대신 Product 측에서 참조하도록 설정

    public static Seller create(Long userId) {
        Seller seller = new Seller();
        seller.userId = userId;
        
        return seller;
    }
}
