package com.github.renuevo.web.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@NoArgsConstructor
public class PaymentCancelDto extends PriceDto{

    @Getter
    @Setter
    @Size(min = 20, max = 20)
    String identityNumber;              //관리번호

}
