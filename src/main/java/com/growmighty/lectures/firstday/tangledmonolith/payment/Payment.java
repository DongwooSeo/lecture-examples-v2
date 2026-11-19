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

    /** 승인 */
    public void approve() {
        checkStatus();
        this.status = PaymentStatus.PAID;
    }

    /** 실패 */
    public void fail() {
        checkStatus();
        this.status = PaymentStatus.FAILED;
    }

    /** 상태 확인 */
    private void checkStatus() {
        if (this.status != PaymentStatus.READY) {
            throw new IllegalStateException("READY 상태에서만 처리 가능합니다.");
        }
    }

}
