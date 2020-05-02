package com.github.renuevo.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;

/**
 * <pre>
 * @className : cardPayDto
 * @author : Deokhwa.Kim
 * @since : 2020-05-01
 * </pre>
 */
@Data
public class CardPayDto {

    @NotNull
    @Min(1000000000L)
    @Max(999999999999L)
    Long number;        //카드번호

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMyy")
    LocalDate validityRange;        //카드 유효기간

    @NotNull
    @Min(100)
    @Max(999)
    Integer cvc;                        //cvc 번호

    @NotNull
    @Min(0)
    @Max(12)
    Integer installment;                //할부

    @NotNull
    @Min(100)
    @Max(1000000000)
    Integer price;                      //결제금액

    @Min(0)
    @Max(10000000)
    Integer tax;                      //부가가치세

    public int getTax() {
        if (Optional.ofNullable(tax).isEmpty()) tax = Math.round(price / 11f);   //부가 가치세 자동 계산
        return tax;
    }

}
