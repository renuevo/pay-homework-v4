package com.github.renuevo.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * @className : PaymentCancelResponseDto
 * @author : Deokhwa.Kim
 * @since : 2020-05-05
 * </pre>
 */
@Getter
@Setter
public class PaymentCancelResponseDto {
    private String identityNumber;      //관리번호
    private int price;                  //취소후 결제된 금액
    private int tax;                    //취소후 결제된 부가가치세
}
