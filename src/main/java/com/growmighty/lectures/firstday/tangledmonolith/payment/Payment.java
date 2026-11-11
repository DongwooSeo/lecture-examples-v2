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

    public void approve() {
        requireReady();
        this.status = PaymentStatus.PAID;
    }

    public void fail() {
        requireReady();
        this.status = PaymentStatus.FAILED;
    }

    private void requireReady() {
        if (this.status != PaymentStatus.READY) {
            throw new IllegalStateException("준비(READY) 상태의 결제만 처리할 수 있습니다. 현재 상태: " + this.status);
        }
    }
}
