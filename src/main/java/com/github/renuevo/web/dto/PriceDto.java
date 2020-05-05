package com.github.renuevo.web.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Setter
@Getter
public class PriceDto {

    @NotNull
    @Min(0)
    @Max(12)
    Integer installment = 0;                //할부

    @NotNull
    @Min(100)
    @Max(1000000000)
    Integer price;                      //결제금액

    @Min(0)
    @Max(10000000)
    Integer tax;                        //부가가치세

    public int getTax() {
        return Optional.ofNullable(tax).orElse(Math.round(price / 11f));    //부가 가치세 자동 계산
    }

}
