package com.github.renuevo;

import com.github.renuevo.common.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.anyString;


/**
 * <pre>
 * @className : PayHomeworkV4ApplicationTests
 * @author : Deokhwa.Kim
 * @since : 2020-05-06
 * @summary : 과제 테스트
 * </pre>
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PayHomeworkV4Application.class)
class PayHomeworkV4ApplicationTests {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    SecurityUtils securityUtils;

    String base = "cardNumber=1234567890123456&validityRange=1125&cvc=777&installment=00";

    @BeforeEach
    public void init() {
        Mockito.when(securityUtils.getHash(anyString())).thenReturn("12341234");
        Mockito.when(securityUtils.getEncode(anyString())).thenReturn("e3740bf7d51ab64938a3502eda9ecc1a2b2412d15a2c2fda3e442f82693b6a184916d2c804a1def4ec2f3d029fc799ee");
        Mockito.when(securityUtils.getDecode(anyString())).thenReturn("1234567890123456,1125,777");
        Mockito.when(securityUtils.getDecode(anyString(), anyString())).thenReturn("1234567890123456,1125,777");
        Mockito.when(securityUtils.getSaltKey()).thenReturn("ce63775ac83101f6");
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/case1.csv")
    void TEST_CASE_1(String type, int price, Integer tax, int status, int resultPrice, int resultTax) {

        //given
        String identityNumber = "7395144a9c0935c22caa";
        Mockito.when(securityUtils.getIdentityNumber()).thenReturn(identityNumber);

        //결제 호출
        switch (type) {
            case "payment":
                webTestClient.post()
                        .uri(uriBuilder -> uriBuilder.path("/payment/save")
                                .query(base)
                                .queryParam("price", price)
                                .queryParam("tax", tax)
                                .build()
                        )
                        .exchange()
                        .expectStatus().isEqualTo(status)
                        .expectBody()
                        .jsonPath("$.price").isEqualTo(resultPrice)
                        .jsonPath("$.tax").isEqualTo(resultTax);
                break;

            //취소 호출
            case "cancel":
                webTestClient.post()
                        .uri(uriBuilder -> uriBuilder.path("/payment/cancel/" + identityNumber)
                                .queryParam("price", price)
                                .queryParam("tax", tax)
                                .build()
                        )
                        .exchange()
                        .expectStatus().isEqualTo(status);
                break;
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/case2.csv")
    void TEST_CASE_2(String type, int price, Integer tax, int status, int resultPrice, int resultTax) {

        //given
        String identityNumber = "7395144a9c0935c22cab";
        Mockito.when(securityUtils.getIdentityNumber()).thenReturn(identityNumber);


        //결제 호출
        switch (type) {
            case "payment":
                webTestClient.post()
                        .uri(uriBuilder -> uriBuilder.path("/payment/save")
                                .query(base)
                                .queryParam("price", price)
                                .queryParam("tax", tax)
                                .build()
                        )
                        .exchange()
                        .expectStatus().isEqualTo(status)
                        .expectBody()
                        .jsonPath("$.price").isEqualTo(resultPrice)
                        .jsonPath("$.tax").isEqualTo(resultTax);
                break;

            //취소 호출
            case "cancel":
                webTestClient.post()
                        .uri(uriBuilder -> uriBuilder.path("/payment/cancel/" + identityNumber)
                                .queryParam("price", price)
                                .queryParam("tax", tax)
                                .build()
                        )
                        .exchange()
                        .expectStatus().isEqualTo(status);
                break;
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/case3.csv")
    void TEST_CASE_3(String type, int price, Integer tax, int status, int resultPrice, int resultTax) {

        //given
        String identityNumber = "7395144a9c0935c22cac";
        Mockito.when(securityUtils.getIdentityNumber()).thenReturn(identityNumber);

        //결제 호출
        switch (type) {
            case "payment":
                webTestClient.post()
                        .uri(uriBuilder -> uriBuilder.path("/payment/save")
                                .query(base)
                                .queryParam("price", price)
                                .queryParam("tax", tax)
                                .build()
                        )
                        .exchange()
                        .expectStatus().isEqualTo(status)
                        .expectBody()
                        .jsonPath("$.price").isEqualTo(resultPrice)
                        .jsonPath("$.tax").isEqualTo(resultTax);
                break;

            //취소 호출
            case "cancel":
                webTestClient.post()
                        .uri(uriBuilder -> uriBuilder.path("/payment/cancel/" + identityNumber)
                                .queryParam("price", price)
                                .queryParam("tax", tax)
                                .build()
                        )
                        .exchange()
                        .expectStatus().isEqualTo(status);
                break;
        }
    }
}
