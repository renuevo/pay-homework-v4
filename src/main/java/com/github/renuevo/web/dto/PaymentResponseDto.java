package com.github.renuevo.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <pre>
 * @className : PaymentResponseDto
 * @author : Deokhwa.Kim
 * @since : 2020-05-03
 * </pre>
 */
@Data
public class PaymentResponseDto {
    String identityNumber;              //관리번호
    int installment;                    //할부개월
    int price;                          //결제금액
    int tax;                            //부가가치세
    LocalDateTime createDt;             //결제일
}
