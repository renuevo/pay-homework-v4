package com.github.renuevo.service;

import com.github.renuevo.PayHomeworkV4Application;
import com.github.renuevo.domain.payment.dto.PaymentCancelDto;
import com.github.renuevo.domain.payment.dto.PaymentDto;
import com.github.renuevo.domain.payment.dto.PaymentResponseDto;
import com.github.renuevo.setup.PaymentDtoBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;

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
    private PaymentWebService paymentWebService;

    private PaymentCancelDto paymentCancelDto1;
    private PaymentCancelDto paymentCancelDto2;

    @BeforeEach
    public void init() {

        //결제 테스트
        PaymentDto paymentDto = PaymentDtoBuilder.build();

        //given
        PaymentResponseDto paymentResponseDto = paymentWebService.paymentSave(paymentDto).block();
        paymentCancelDto1 = new PaymentCancelDto();
        paymentCancelDto2 = new PaymentCancelDto();

        assert paymentResponseDto != null;
        paymentCancelDto1.setIdentityNumber(paymentResponseDto.getIdentityNumber());
        paymentCancelDto2.setIdentityNumber(paymentResponseDto.getIdentityNumber());

        paymentCancelDto1.setPrice(1000);
        paymentCancelDto2.setPrice(1000);

        StepVerifier.setDefaultTimeout(Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("같은 카드 다중 결제 테스트")
    public void 다중결제_테스트() {

        //when
        //다중 호출 Mono
        var PaymentZipMono = Mono.zip(
                paymentWebService.paymentSave(PaymentDtoBuilder.build()),
                paymentWebService.paymentSave(PaymentDtoBuilder.build()),
                paymentWebService.paymentSave(PaymentDtoBuilder.build()))
                .subscribeOn(Schedulers.parallel());

        //then
        StepVerifier
                .create(PaymentZipMono)
                .verifyError(); //에러 발생여부 확인
    }


    @Test
    @DisplayName("동시 캔슬 테스트")
    public void 동시_캔슬_테스트() {
        //when
        var cancelMono = Mono.zip(
                paymentWebService.paymentCancel(paymentCancelDto1),
                paymentWebService.paymentCancel(paymentCancelDto2))
                .subscribeOn(Schedulers.parallel());

        //then
        StepVerifier.
                create(cancelMono)
                .verifyError(); //에러 발생여부 확인
    }


    @Test
    @DisplayName("순차 캔슬 테스트")
    public void 순차_캔슬_테스트() {
        //when
        var cancelMono = Mono.just(paymentWebService.paymentCancel(paymentCancelDto1))
                .concatWith(Mono.just(paymentWebService.paymentCancel(paymentCancelDto1)));

        //then
        StepVerifier.
                create(cancelMono)
                .assertNext(Assertions::assertNotNull)
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();
    }


}