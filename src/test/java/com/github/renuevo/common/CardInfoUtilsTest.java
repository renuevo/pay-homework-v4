package com.github.renuevo.common;

import com.github.renuevo.web.dto.CardInfoDto;
import com.github.renuevo.web.dto.CardPayDto;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AbstractArrayAssert.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class CardInfoUtilsTest {

    @Autowired
    private CardInfoUtils cardInfoUtils;

    @Test
    void getCardEncryptTest() {

        Logger log = LoggerFactory.getLogger(this.getClass());

        //given
        List<CardPayDto> cardPayDtoList = List.of(
                new CardPayDto(1234567890123456L, 777, "1125"),
                new CardPayDto(9123421390123456L, 666, "0110"),
                new CardPayDto(4355327890123456L, 555, "1201")
        );

        //when
        List<String> cardEncrypList = cardPayDtoList.stream().map(cardInfoUtils::getCardEncrypt).collect(Collectors.toList());
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
                    .isEqualToComparingFieldByField(cardPayDtoList.get(i));
        }

    }

    @Test
    void getPaymentInfo() {

        //given


        //when


        //then


    }
}