package com.github.renuevo.service;

import com.github.renuevo.PayHomeworkV4Application;
import com.github.renuevo.domain.payment.dto.PaymentCancelDto;
import com.github.renuevo.domain.payment.dto.PaymentDto;
import com.github.renuevo.domain.payment.dto.PaymentResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

/**
 * <pre>
 * @className : paymentWebServiceTest
 * @author : Deokhwa.Kim
 * @since : 2020-05-06
 * </pre>
 */
@ExtendWith(SpringExtension.class)
@DisplayName("전체 비즈니스 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PayHomeworkV4Application.class)
class PaymentWebServiceTest {

    @Autowired
    PaymentWebService paymentWebService;

    @Autowired
    ModelMapper modelMapper;

    PaymentDto paymentDto1;
    PaymentDto paymentDto2;

    PaymentCancelDto paymentCancelDto1;
    PaymentCancelDto paymentCancelDto2;

    @BeforeEach
    public void init() {

        //결제 테스트
        paymentDto1 = PaymentDto.builder()
                .cardNumber("1234567890123456")
                .cvc(555)
                .installment(0)
                .validityRange("1125")
                .price(10000000)
                .build();
        paymentDto2 = modelMapper.map(paymentDto1, PaymentDto.class);

        //캔슬 테스트
        PaymentResponseDto paymentResponseDto = paymentWebService.paymentSave(paymentDto1).block();
        PaymentCancelDto paymentCancelDto1 = new PaymentCancelDto();
        PaymentCancelDto paymentCancelDto2 = new PaymentCancelDto();

        assert paymentResponseDto != null;
        paymentCancelDto1.setIdentityNumber(paymentResponseDto.getIdentityNumber());
        paymentCancelDto1.setPrice(10000);
        paymentCancelDto2.setIdentityNumber(paymentResponseDto.getIdentityNumber());
        paymentCancelDto2.setPrice(10000);

    }

    @Test
    @DisplayName("같은 카드 다중결제 테스트")
    void 다중결제_테스트() {

        //다중 호출 Mono
        var PaymentZipMono = Mono.zip(
                paymentWebService.paymentSave(paymentDto1),
                paymentWebService.paymentSave(paymentDto2))
                .subscribeOn(Schedulers.parallel());

        //when & then
        StepVerifier.
                create(PaymentZipMono)
                .verifyError(); //에러 발생여부 확인
    }


    @Test
    @DisplayName("동시 캔슬 테스트")
    void 동시_캔슬_테스트() {

        //given
        var cancelMono = Mono.zip(
                paymentWebService.paymentCancel(paymentCancelDto1),
                paymentWebService.paymentCancel(paymentCancelDto2))
                .subscribeOn(Schedulers.parallel());

        //when & then
        StepVerifier.
                create(cancelMono)
                .verifyError(); //에러 발생여부 확인
    }


    @Test
    @DisplayName("순차 캔슬 테스트")
    void 순차_캔슬_테스트() {

        //given
        var cancelMono = Mono.just(paymentWebService.paymentCancel(paymentCancelDto1))
                .concatWith(Mono.just(paymentWebService.paymentCancel(paymentCancelDto1)));

        //when & then
        StepVerifier.
                create(cancelMono)
                .assertNext(Assertions::assertNotNull)
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();
    }


}