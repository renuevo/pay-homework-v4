package com.github.renuevo.setup;

import com.github.renuevo.domain.payment.dto.PaymentDto;

public class PaymentDtoBuilder {

    public static PaymentDto build() {
        return PaymentDto.builder()
                .cardNumber("1234567890123456")
                .installment(0)
                .cvc(777)
                .price(300000)
                .validityRange("1125")
                .tax(30000)
                .build();
    }


    public static PaymentDto build(String cardNumber, int installment, int cvc, int price, int tax, String validityRange) {
        return PaymentDto.builder()
                .cardNumber(cardNumber)
                .installment(installment)
                .cvc(cvc)
                .price(price)
                .validityRange(validityRange)
                .tax(tax)
                .build();
    }

}
