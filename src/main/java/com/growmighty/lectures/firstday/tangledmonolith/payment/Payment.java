package com.growmighty.lectures.firstday.tangledmonolith.payment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    public static Payment ready(BigDecimal amount) {
        Payment payment = new Payment();
        payment.amount = amount;
        payment.status = PaymentStatus.READY;
        return payment;
    }

    /*
    public void changeStatus(PaymentStatus status) {
        this.status = status;
    }
     */


    // READY 상태인지 검증
    private void validateReady() {
        if (this.status != PaymentStatus.READY) {
            throw new IllegalStateException("READY 상태에서만 변경할 수 있습니다.");
        }
    }

    // 결제 승인
    public void approve() {
        validateReady();
        this.status = PaymentStatus.PAID;
    }

    // 결제 실패
    public void fail() {
        validateReady();
        this.status = PaymentStatus.FAILED;
    }
}
