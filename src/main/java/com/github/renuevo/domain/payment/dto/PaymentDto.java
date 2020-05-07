package com.github.renuevo.domain.payment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.github.renuevo.domain.payment.common.ValidationConstant.*;

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
public class PaymentDto extends PriceDto {

    @NotNull
    @Size(min = MIN_CARD_NUMBER_LENGTH, max = MAX_CARD_NUMBER_LENGTH)
    private String cardNumber;                    //카드번호

    @NotNull
    @Size(min = CARD_VALIDITY_RANGE_LENGTH, max = CARD_VALIDITY_RANGE_LENGTH)
    private String validityRange;            //카드 유효기간

    @NotNull
    @Min(MIN_CVC)
    @Max(MAX_CVC)
    private Integer cvc;                        //cvc 번호

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
