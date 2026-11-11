package com.growmighty.lectures.firstday.tangledmonolith.homework;

import com.growmighty.lectures.firstday.tangledmonolith.payment.Payment;
import com.growmighty.lectures.firstday.tangledmonolith.payment.PaymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** [과제 DDD-2 정답 예시] 결제 상태 전이 테스트 */
class PaymentStatusTest {

    @Test
    @DisplayName("준비(READY) 상태의 결제를 승인하면 PAID 가 된다")
    void 준비_상태를_승인한다() {
        Payment payment = Payment.ready(BigDecimal.valueOf(10000));

        payment.approve();

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PAID);
    }

    @Test
    @DisplayName("준비 상태의 결제를 실패 처리하면 FAILED 가 된다")
    void 준비_상태를_실패_처리한다() {
        Payment payment = Payment.ready(BigDecimal.valueOf(10000));

        payment.fail();

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAILED);
    }

    @Test
    @DisplayName("이미 승인된 결제는 다시 승인하거나 실패 처리할 수 없다")
    void 승인된_결제는_상태를_바꿀_수_없다() {
        Payment payment = Payment.ready(BigDecimal.valueOf(10000));
        payment.approve();

        assertThatThrownBy(payment::approve).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(payment::fail).isInstanceOf(IllegalStateException.class);
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PAID);
    }
}
