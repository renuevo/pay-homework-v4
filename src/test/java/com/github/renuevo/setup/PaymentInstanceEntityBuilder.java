package com.github.renuevo.setup;

import com.github.renuevo.domain.payment.dao.PaymentInstanceEntity;

public class PaymentInstanceEntityBuilder {

    public static PaymentInstanceEntity newBuild() {
        return PaymentInstanceEntity.builder()
                .key(null)
                .identityNumber("2b44fddfa091bc5c12d0")
                .cardInfo("c75d2519eba6b3b99495547074bc7f6fb1541375261e0f2d68db714cbc1312bb18ce41313e18ab0611bf5679a7b4b279")
                .salt("809145b376e6fffd")
                .cancelIdentityNumber("5a22c93c57d3ec09cada")
                .installment(0)
                .price(300000)
                .tax(10)
                .build();
    }

    public static PaymentInstanceEntity build() {
        return PaymentInstanceEntity.builder()
                .key(1L)
                .identityNumber("2b44fddfa091bc5c12d0")
                .cardInfo("c75d2519eba6b3b99495547074bc7f6fb1541375261e0f2d68db714cbc1312bb18ce41313e18ab0611bf5679a7b4b279")
                .salt("809145b376e6fffd")
                .cancelIdentityNumber("5a22c93c57d3ec09cada")
                .installment(0)
                .price(300000)
                .tax(10)
                .build();
    }
}
