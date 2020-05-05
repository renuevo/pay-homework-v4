package com.github.renuevo.domain;

import com.github.renuevo.domain.PaymentComponent;
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
class PaymentComponentTest {

    @Autowired
    private PaymentComponent paymentComponent;

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

        //given
        List<PaymentDto> paymentDtoList = List.of(
                PaymentDto.builder().cardNumber("1234567890123456").cvc(777).validityRange("1125").build(),
                PaymentDto.builder().cardNumber("9123421390123456").cvc(666).validityRange("0110").build(),
                PaymentDto.builder().cardNumber("4355327890123456").cvc(555).validityRange("1201").build()
        );

        //when
        List<String> cardEncrypList = paymentDtoList.stream().map(paymentComponent::getCardEncrypt).collect(Collectors.toList());
        List<CardInfoDto> resultList = cardEncrypList.stream().map(data -> paymentComponent.getCardDecrypt(data, paymentComponent.getSalt())).collect(Collectors.toList());

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