package com.github.renuevo.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;

@Data
@NoArgsConstructor
public class PaymentCancelDto {

    @Size(min = 20, max = 20)
    String identityNumber;              //관리번호

    @NotNull
    @Min(100)
    @Max(1000000000)
    Integer price;                      //결제금액

    @Min(0)
    @Max(10000000)
    Integer tax;                      //부가가치세

    @Builder
    public PaymentCancelDto(String identityNumber, int price, int tax){
        this.identityNumber = identityNumber;
        this.price = price;
        this.tax = tax;
    }

    public int getTax() {
        return Optional.ofNullable(tax).orElse(Math.round(price / 11f)); //부가 가치세 자동 계산
    }
}
