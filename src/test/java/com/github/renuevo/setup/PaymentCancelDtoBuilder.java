package com.github.renuevo.setup;


import com.github.renuevo.domain.payment.dto.PaymentCancelDto;

public class PaymentCancelDtoBuilder {

    public static PaymentCancelDto build() {
        PaymentCancelDto paymentCancelDto = new PaymentCancelDto();
        paymentCancelDto.setTax(10);
        paymentCancelDto.setPrice(1000);
        paymentCancelDto.setIdentityNumber("2b44fddfa091bc5c12d0");
        paymentCancelDto.setInstallment(0);
        return paymentCancelDto;
    }


}
