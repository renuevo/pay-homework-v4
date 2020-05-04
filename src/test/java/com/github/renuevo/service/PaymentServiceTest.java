package com.github.renuevo.service;

import com.github.renuevo.web.dto.PaymentCancelDto;
import com.github.renuevo.web.dto.PaymentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;

@SpringBootTest
class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;

    @Test
    void paymentCall() {
        var mono = paymentService.paymentCall(PaymentDto.builder()
                .cardNumber(1234567890123456L)
                .cvc(555)
                .installment(0)
                .validityRange(LocalDate.now())
                .price(10000000)
                .build())
                .publishOn(Schedulers.parallel())
                .zipWhen(paymentResponseDto ->
                Mono.zip(
                        paymentService.paymentCancel(PaymentCancelDto.builder()
                                .identityNumber(paymentResponseDto.getIdentityNumber())
                                .price(1000)
                                .build()),
                        paymentService.paymentCancel(PaymentCancelDto.builder()
                                .identityNumber(paymentResponseDto.getIdentityNumber())
                                .price(1000)
                                .build()),
                        paymentService.paymentCancel(PaymentCancelDto.builder()
                                .identityNumber(paymentResponseDto.getIdentityNumber())
                                .price(1000)
                                .build()),
                        paymentService.paymentCancel(PaymentCancelDto.builder()
                                .identityNumber(paymentResponseDto.getIdentityNumber())
                                .price(1000)
                                .build()),
                        paymentService.paymentCancel(PaymentCancelDto.builder()
                                .identityNumber(paymentResponseDto.getIdentityNumber())
                                .price(1000)
                                .build()),
                        paymentService.paymentCall(PaymentDto.builder()
                                .cardNumber(214213123456L)
                                .cvc(555)
                                .installment(0)
                                .validityRange(LocalDate.now())
                                .price(1000000)
                                .build())
                ))
                .flatMap(tuple -> Mono.just(tuple.getT1()))
                .block();


        System.out.println("test");

        //1. validation 체크를 한다

        //2. 카드 정보를 암호화 한다

        //3. 결제 관리저장

        //4. 결제 내역저장

        //5. 카드사 통신

    }

}