package com.github.renuevo.common;

import com.github.renuevo.web.dto.CardInfoDto;
import com.github.renuevo.web.dto.PaymentDto;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

    /**
     * <pre>
     *  @methodName : getCardEncrypt
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-03 오후 1:54
     *  @summary : 카드정보 암호화
     *  @param : [cardPayDto]
     *  @return : java.lang.String
     * </pre>
     */
    public String getCardEncrypt(PaymentDto paymentDto) {
        return securityUtils.getEncode(paymentDto.getNumber() + "," + paymentDto.getValidityRangeStr() + "," + paymentDto.getCvc());
    }

    /**
     * <pre>
     *  @methodName : getCardDecrypt
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-03 오후 1:52
     *  @summary : 카드정보 복호화
     *  @param : [digest, salt]
     *  @return : com.github.renuevo.web.dto.CardInfoDto
     * </pre>
     */
    public CardInfoDto getCardDecrypt(String digest, String salt) throws Exception {
        //복호화
        String[] cardInfoArray = securityUtils.getDecode(digest, salt).split(",");

        if (cardInfoArray.length < 3)
            throw new Exception();

        Long number = Optional.of(cardInfoArray[0]).map(Long::parseLong).get();
        LocalDate validityRange = Optional.of(cardInfoArray[1]).map(date -> LocalDate.of(LocalDate.now().getYear() - (LocalDate.now().getYear() / 100) + Integer.parseInt(date.substring(2, 4)), Integer.parseInt(date.substring(0, 2)), 1)).get();
        Integer cvc = Optional.of(cardInfoArray[2]).map(Integer::parseInt).get();

        return new CardInfoDto(number, validityRange, cvc);
    }

    public String getSalt() {
        return securityUtils.getSaltKey();
    }

    /**
     * <pre>
     *  @methodName : getPaymentInfo
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-03 오후 1:54
     *  @summary : 카드사 통신 코드 생성
     *  @param : [cardPayDto, cardActionType, key]
     *  @return : java.lang.String
     * </pre>
     */
    public String getPaymentInfo(PaymentDto paymentDto, CardActionType cardActionType, long key) {
        StringBuilder paymentInfo = new StringBuilder(Strings.padEnd(cardActionType.name(), 10, ' '));  //결제타입
        paymentInfo.append(Strings.padStart(String.valueOf(key), 20, '0'));  //관리번호
        paymentInfo.append(Strings.padEnd(String.valueOf(paymentDto.getNumber()), 20, ' '));  //카드번호
        paymentInfo.append(Strings.padStart(String.valueOf(paymentDto.getInstallment()), 2, '0'));  //할부일수
        paymentInfo.append(paymentDto.getValidityRangeStr());  //유효기간
        paymentInfo.append(paymentDto.getCvc());  //cvc
        paymentInfo.append(Strings.padStart(String.valueOf(paymentDto.getPrice()), 10, ' '));  //결제금액
        paymentInfo.append(Strings.padStart(String.valueOf(paymentDto.getTax()), 10, '0'));  //부가가치세
        paymentInfo.append(Strings.padStart("", 20, ' '));  //최소관리번호
        paymentInfo.append(Strings.padEnd(getCardEncrypt(paymentDto), 300, ' '));  //카드 정보 암호화
        paymentInfo.append(Strings.padEnd("", 47, ' '));  //예비구간
        return securityUtils.getSaltKey();
    }

}
