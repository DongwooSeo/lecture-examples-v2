package com.growmighty.lectures.firstday.tangledmonolith.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Payment pay(BigDecimal totalAmount) {
        Payment payment = Payment.ready(totalAmount);
        //payment.changeStatus(PaymentStatus.PAID);
        // -> 결제 상태를 직접 지정.
        payment.approve();
        // -> 결제 승인 요청, Payment엔티티가 상태 변경 가능 여부 판단
        paymentRepository.save(payment);
        return payment;
    }
}
