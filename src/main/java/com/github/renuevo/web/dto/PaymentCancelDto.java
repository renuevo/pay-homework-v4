package com.github.renuevo.web.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class PaymentCancelDto extends PriceDto {

    @Size(min = 20, max = 20)
    String identityNumber;              //관리번호

    public boolean taxCheck() {
        return this.tax == null;
    }

}
