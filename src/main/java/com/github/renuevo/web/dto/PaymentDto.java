package com.github.renuevo.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * <pre>
 * @className : cardPayDto
 * @author : Deokhwa.Kim
 * @since : 2020-05-01
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    @NotNull
    @Min(1000000000L)
    @Max(9999999999999999L)
    Long number;        //카드번호

    @NotNull
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


    public PaymentDto(long number, int cvc, String validityRange) {
        this.number = number;
        this.cvc = cvc;
        setValidityRange(validityRange);
    }

    public void setValidityRange(String date) {
        this.validityRange = LocalDate.of(LocalDate.now().getYear()  - (LocalDate.now().getYear() / 100) + Integer.parseInt(date.substring(2, 4)), Integer.parseInt(date.substring(0, 2)), 1);
    }

    public String getValidityRangeStr() {
        return validityRange.format(DateTimeFormatter.ofPattern("MMyy"));
    }

    public int getTax() {
        return Optional.ofNullable(tax).orElse(Math.round(price / 11f)); //부가 가치세 자동 계산
    }

}
