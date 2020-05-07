package com.github.renuevo.setup;

import com.github.renuevo.domain.payment.common.PaymentActionType;
import com.github.renuevo.domain.payment.dao.PaymentDetailEntity;

public class PaymentDetailEntityBuilder {

    public static PaymentDetailEntity paymentBuild() {
        return PaymentDetailEntity.builder()
                .key(1L)
                .identityNumber("2b44fddfa091bc5c12d0")
                .paymentType(PaymentActionType.PAYMENT.name())
                .installment(0)
                .price(300000)
                .tax(30000)
                .build();
    }

    public static PaymentDetailEntity cancelBuild() {
        return PaymentDetailEntity.builder()
                .key(1L)
                .identityNumber("2b44fddfa091bc5c12d0")
                .paymentType(PaymentActionType.CANCEL.name())
                .installment(0)
                .price(1000)
                .tax(100)
                .build();
    }

    public static PaymentDetailEntity newPaymentBuild() {
        return PaymentDetailEntity.builder()
                .key(null)
                .identityNumber("2b44fddfa091bc5c12d0")
                .paymentType(PaymentActionType.PAYMENT.name())
                .installment(0)
                .price(300000)
                .tax(30000)
                .build();
    }

}
