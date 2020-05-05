package com.github.renuevo.web.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;

/**
 * <pre>
 * @className : cardPayDto
 * @author : Deokhwa.Kim
 * @since : 2020-05-01
 * </pre>
 */

@Setter
@Getter
@NoArgsConstructor
public class PaymentDto extends PriceDto{

    @NotNull
    @Size(min = 10, max = 16)
    String cardNumber;                    //카드번호

    @NotNull
    @Size(min = 4, max = 4)
    String validityRange;            //카드 유효기간

    @NotNull
    @Min(100)
    @Max(999)
    Integer cvc;                        //cvc 번호

    @Builder
    public PaymentDto(String cardNumber, String validityRange, int cvc, int installment, int price, int tax) {
        this.cardNumber = cardNumber;
        this.validityRange = validityRange;
        this.cvc = cvc;
        this.installment = installment;
        this.price = price;
        this.tax = tax;
    }

}
