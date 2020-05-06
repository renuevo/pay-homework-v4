package com.github.renuevo.web.dto;

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
    String identityNumber;      //관리번호
    int price;                  //취소후 결제된 금액
    int tax;                    //취소후 결제된 부가가치세
}
