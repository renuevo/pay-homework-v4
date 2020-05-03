package com.github.renuevo.common;

import com.github.renuevo.web.dto.CardInfoDto;
import com.github.renuevo.web.dto.CardPayDto;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <pre>
 * @className : CardInfoUtils
 * @author : Deokhwa.Kim
 * @since : 2020-05-03
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class CardInfoUtils {

    private final SecurityUtils securityUtils;

    public String getCardEncrypt(CardPayDto cardPayDto) {
        return securityUtils.getEncode(cardPayDto.getNumber() + "," + cardPayDto.getValidityRangeStr() + "," + cardPayDto.getCvc());
    }

    public CardInfoDto getCardDecrypt(String digest, String salt) throws Exception {
        String[] cardInfoArray = securityUtils.getDecode(digest, salt).split(",");
        Long number = null;
        LocalDate validityRange = null;
        Integer cvc = null;

        try {

            if (cardInfoArray.length < 3)
                throw new Exception();

            number = Optional.of(cardInfoArray[0]).map(Long::parseLong).get();
            validityRange = Optional.of(cardInfoArray[1]).map(date -> LocalDate.of(LocalDate.now().getYear()  - (LocalDate.now().getYear() / 100) + Integer.parseInt(date.substring(2,4)), Integer.parseInt(date.substring(0, 2)), 1)).get();
            cvc = Optional.of(cardInfoArray[2]).map(Integer::parseInt).get();

        } catch (Exception e) {

        }
        return new CardInfoDto(number, validityRange, cvc);
    }

    public String getSalt() {
        return securityUtils.getSaltKey();
    }

    public String getPaymentInfo(CardPayDto cardPayDto, CardActionType cardActionType, long key) {
        StringBuilder paymentInfo = new StringBuilder(Strings.padEnd(cardActionType.name(), 10, ' '));  //결제타입
        paymentInfo.append(Strings.padStart(String.valueOf(key), 20, '0'));  //관리번호
        paymentInfo.append(Strings.padEnd(String.valueOf(cardPayDto.getNumber()), 20, ' '));  //카드번호
        paymentInfo.append(Strings.padStart(String.valueOf(cardPayDto.getInstallment()), 2, '0'));  //할부일수
        paymentInfo.append(cardPayDto.getValidityRangeStr());  //유효기간
        paymentInfo.append(cardPayDto.getCvc());  //cvc
        paymentInfo.append(Strings.padStart(String.valueOf(cardPayDto.getPrice()), 10, ' '));  //결제금액
        paymentInfo.append(Strings.padStart(String.valueOf(cardPayDto.getTax()), 10, '0'));  //부가가치세
        paymentInfo.append(Strings.padStart("", 20, ' '));  //최소관리번호
        paymentInfo.append(Strings.padEnd(getCardEncrypt(cardPayDto), 300, ' '));  //카드 정보 암호화
        paymentInfo.append(Strings.padEnd("", 47, ' '));  //예비구간
        return securityUtils.getSaltKey();
    }

}
