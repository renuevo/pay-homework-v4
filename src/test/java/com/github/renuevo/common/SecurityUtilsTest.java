package com.github.renuevo.common;

import static org.assertj.core.api.Assertions.*;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class SecurityUtilsTest {

    @Autowired
    SecurityUtils securityUtils;

    /**
     * <pre>
     *  @methodName : encryptTest
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-03 오전 12:14
     *  @summary : 암호화 모듈 테스트
     *  @param : []
     *  @return : void
     * </pre>
     */
    @Test
    public void encryptTest() {

        //given
        List<String> cardInfoList = List.of("121314141224", "212412412213123", "321412312312");

        //when
        List<String> digestList = cardInfoList.stream().map(text -> securityUtils.getEncode(text)).collect(Collectors.toList());

        //then
        assertThat(cardInfoList)
                .as("암호화 테스트")
                .doesNotContain(digestList.toArray(new String[0]));
        assertThat(cardInfoList)
                .as("복호화 테스트")
                .containsExactly(digestList.stream().map(text -> securityUtils.getDecode(text)).toArray(String[]::new));
    }
}