package com.github.renuevo.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentViewResponseDto {

    private CardInfoDto cardInfoDto;        //카드정보
    private String paymentType;             //결제 / 취소
    private LocalDateTime createDt;        //생성일
    private int price;                      //금액
    private int tax;                        //부가가치세
    private int resultPrice;                //남은 금액
    private int resultTax;                  //남은 부가가치세
    private int resultInstallment;          //할부 개월

}
