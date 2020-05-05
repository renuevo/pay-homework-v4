package com.github.renuevo.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardInfoDto {

    String cardNumber;                //카드번호
    String validityRange;        //카드 유효기간
    int cvc;                    //cvc 번호

}
