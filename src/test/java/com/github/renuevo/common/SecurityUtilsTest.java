package com.github.renuevo.common;

import com.github.renuevo.PayHomeworkV4Application;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <pre>
 * @className : SecurityUtilsTest
 * @author : Deokhwa.Kim
 * @since : 2020-05-06
 * </pre>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PayHomeworkV4Application.class)
class SecurityUtilsTest {

    @Autowired
    SecurityUtils securityUtils;

    @Test
    @DisplayName("암복호화 모듈 테스트 진행")
    public void 암복호화_테스트() {

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