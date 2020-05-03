package com.github.renuevo.common;

import com.github.renuevo.web.dto.CardInfoDto;
import com.github.renuevo.web.dto.PaymentDto;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <pre>
 * @className : CardInfoUtilsTest
 * @author : Deokhwa.Kim
 * @since : 2020-05-03
 * </pre>
 */
@SpringBootTest
class CardInfoUtilsTest {

    @Autowired
    private CardInfoUtils cardInfoUtils;

    /**
     * <pre>
     *  @methodName : getCardEncryptTest
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-03 오후 3:02
     *  @summary : 카드 암복호화 테스트
     *  @param : []
     *  @return : void
     * </pre>
     */
    @Test
    void getCardEncryptTest() {

        Logger log = LoggerFactory.getLogger(this.getClass());

        //given
        List<PaymentDto> paymentDtoList = List.of(
                new PaymentDto(1234567890123456L, 777, "1125"),
                new PaymentDto(9123421390123456L, 666, "0110"),
                new PaymentDto(4355327890123456L, 555, "1201")
        );

        //when
        List<String> cardEncrypList = paymentDtoList.stream().map(cardInfoUtils::getCardEncrypt).collect(Collectors.toList());
        List<CardInfoDto> resultList = cardEncrypList.stream().map(data -> {
            CardInfoDto cardInfoDto = null;
            try {
                cardInfoDto = cardInfoUtils.getCardDecrypt(data, cardInfoUtils.getSalt());
            } catch (Exception e) {
                log.error("getCardDecrypt Error {}", e.getMessage(), e);
            }
            return cardInfoDto;
        }).collect(Collectors.toList());

        //then
        for (int i = 0; i < cardEncrypList.size(); i++) {
            assertThat(resultList.get(i))
                    .as("카드 정보 암호화/복호화 테스트")
                    .isEqualToComparingFieldByField(paymentDtoList.get(i));
        }

    }

    @Test
    void getPaymentInfo() {

        //given


        //when


        //then


    }
}