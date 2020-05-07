package com.github.renuevo.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardInfoDto {

    String cardNumber;                //카드번호
    String validityRange;        //카드 유효기간
    int cvc;                    //cvc 번호

}
