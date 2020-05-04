package com.github.renuevo.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PaymentCancelResponseDto {
    String identityNumber;      //관리번호
    int installment;            //할부개월
    int price;                  //취소후 결제된 금액
    int tax;                    //취소후 결제된 부가가치세
}
