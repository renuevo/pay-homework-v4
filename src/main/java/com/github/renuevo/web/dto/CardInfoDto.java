package com.github.renuevo.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardInfoDto {

    Long number;        //카드번호
    LocalDate validityRange;        //카드 유효기간
    Integer cvc;                        //cvc 번호

}
