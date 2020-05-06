package com.github.renuevo.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <pre>
 * @className : PaymentResponseDto
 * @author : Deokhwa.Kim
 * @since : 2020-05-03
 * </pre>
 */
@Getter
@Setter
public class PaymentResponseDto {
    String identityNumber;              //관리번호
    int price;                          //결제금액
    int tax;                            //부가가치세
    LocalDateTime createDt;             //결제일
}
