package com.growmighty.lectures.firstday.tangledmonolith.homework;

import com.growmighty.lectures.firstday.tangledmonolith.payment.Payment;
import com.growmighty.lectures.firstday.tangledmonolith.payment.PaymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * [과제 DDD-2] 결제 상태 전이 테스트 — 뼈대입니다.
 *
 * 지금 Payment 는 changeStatus(아무 상태) 로 밖에서 마음대로 바꿀 수 있습니다.
 * changeStatus 를 없애고 approve() / fail() 처럼 "의미 있는 행위"로 바꾼 뒤 테스트를 채우세요.
 */
class PaymentStatusTest {

    @Test
    @DisplayName("준비(READY) 상태의 결제를 승인하면 PAID 가 된다")
    void 준비_상태를_승인한다() {
        // given
        Payment payment = Payment.ready(BigDecimal.valueOf(1000));

        // when
        payment.approve();

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PAID);
    }

    @Test
    @DisplayName("준비 상태의 결제를 실패 처리하면 FAILED 가 된다")
    void 준비_상태를_실패_처리한다() {
        // given
        Payment payment = Payment.ready(BigDecimal.valueOf(1000));

        // when
        payment.fail();

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAILED);
    }

    @Test
    @DisplayName("이미 승인된 결제는 다시 승인하거나 실패 처리할 수 없다")
    void 승인된_결제는_상태를_바꿀_수_없다() {
        // given
        Payment payment = Payment.ready(BigDecimal.valueOf(1000));
        payment.approve();

        // when & then
        // 승인 시도
        assertThatThrownBy(() -> payment.approve())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("READY 상태에서만 처리 가능합니다.");
        // 실패 처리 시도
        assertThatThrownBy(() -> payment.fail())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("READY 상태에서만 처리 가능합니다.");

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PAID);
    }
}
