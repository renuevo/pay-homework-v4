package com.github.renuevo.web.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;

@Data
public class PaymentCancelDto {

    @Size(min = 20, max = 20)
    String number;              //관리번호

    @NotNull
    @Min(100)
    @Max(1000000000)
    Integer price;                      //결제금액

    @Min(0)
    @Max(10000000)
    Integer tax;                      //부가가치세

    public int getTax() {
        return Optional.ofNullable(tax).orElse(Math.round(price / 11f)); //부가 가치세 자동 계산
    }
}
