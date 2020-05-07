package com.github.renuevo.domain.payment.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

import static com.github.renuevo.domain.payment.common.ValidationConstant.IDENTITY_NUMBER_LENGTH;

@Getter
@Setter
@NoArgsConstructor
public class PaymentCancelDto extends PriceDto {

    @Size(min = IDENTITY_NUMBER_LENGTH, max = IDENTITY_NUMBER_LENGTH)
    private String identityNumber;              //관리번호

    public boolean taxCheck() {
        return this.tax == null;
    }

}
