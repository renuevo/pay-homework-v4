package com.github.renuevo.domain;

import com.github.renuevo.common.DataCommon;
import com.github.renuevo.common.SecurityUtils;
import com.github.renuevo.web.dto.CardInfoDto;
import com.github.renuevo.web.dto.PaymentCancelDto;
import com.github.renuevo.web.dto.PaymentDto;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * <pre>
 * @className : CardInfoUtils
 * @author : Deokhwa.Kim
 * @since : 2020-05-03
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class PaymentComponent {

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
        return securityUtils.getEncode(paymentDto.getCardNumber() + "," + paymentDto.getValidityRangeStr() + "," + paymentDto.getCvc());
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
    private String getPaymentInfo(PaymentDto paymentDto, PaymentActionType paymentActionType, String mainIdentityNumber, String subIdentityNumber) {
        StringBuilder paymentInfo = new StringBuilder(Strings.padEnd(paymentActionType.name(), DataCommon.CARD_ACTION, ' '))  //결제타입
                .append(mainIdentityNumber)  //주체 관리번호
                .append(Strings.padEnd(String.valueOf(paymentDto.getCardNumber()), DataCommon.CARD_NUMBER, ' '))  //카드번호
                .append(Strings.padStart(String.valueOf(paymentDto.getInstallment()), DataCommon.INSTALLMENT, '0'))  //할부일수
                .append(paymentDto.getValidityRangeStr())  //유효기간
                .append(paymentDto.getCvc())  //cvc
                .append(Strings.padStart(String.valueOf(paymentDto.getPrice()), DataCommon.PRICE, ' '))  //결제금액
                .append(Strings.padStart(String.valueOf(paymentDto.getTax()), DataCommon.TAX, '0'))  //부가가치세
                .append(Strings.padStart(subIdentityNumber, DataCommon.CANCEL_NUMBER, ' '))  //서브 관리번호
                .append(Strings.padEnd(getCardEncrypt(paymentDto), DataCommon.CARD_ENCRYPT, ' '))  //카드 정보 암호화
                .append(Strings.padEnd("", DataCommon.SPARE, ' '));  //예비구간
        paymentInfo.insert(0, Strings.padStart(String.valueOf(paymentInfo.length()), DataCommon.DATA_LENGTH, ' '));   //데이터 길이 계산
        return paymentInfo.toString();
    }

    public String getPaymentInfo(PaymentDto paymentDto, PaymentActionType paymentActionType, String identityNumber) {
        return this.getPaymentInfo(paymentDto, paymentActionType, identityNumber, "");
    }

    public String getCancelPaymentInfo(PaymentCancelDto paymentCancelDto, CardInfoDto cardInfoDto, PaymentActionType paymentActionType, String identityNumber, String cancelIdentityNumber) {
        //Data Bind
        PaymentDto paymentDto = PaymentDto.builder()
                .cardNumber(cardInfoDto.getCardNumber())
                .cvc(cardInfoDto.getCvc())
                .validityRange(cardInfoDto.getValidityRange())
                .price(paymentCancelDto.getPrice())
                .tax(paymentCancelDto.getTax())
                .installment(0)
                .build();
        return this.getPaymentInfo(paymentDto, paymentActionType, cancelIdentityNumber, identityNumber);
    }


    public String getIdentityNumber() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(24) + securityUtils.getIdentityHash(uuid);
    }

}
