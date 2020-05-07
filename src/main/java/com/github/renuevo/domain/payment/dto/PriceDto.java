package com.github.renuevo.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

import static com.github.renuevo.domain.payment.common.ValidationConstant.*;

@Setter
@Getter
public class PriceDto {

    @NotNull
    @Min(MIN_INSTALLMENT_AMOUNT)
    @Max(MAX_INSTALLMENT_AMOUNT)
    Integer installment = 0;                //할부

    @NotNull
    @Min(MIN_PAYMENT_PRICE_AMOUNT)
    @Max(MAX_PAYMENT_PRICE_AMOUNT)
    Integer price;                      //결제금액

    @Min(MIN_PAYMENT_TAX_AMOUNT)
    @Max(MAX_PAYMENT_TAX_AMOUNT)
    Integer tax;                        //부가가치세

    public int getTax() {
        return Optional.ofNullable(tax).orElse(Math.round(price / 11f));    //부가 가치세 자동 계산
    }

}
