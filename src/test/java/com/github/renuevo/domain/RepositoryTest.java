package com.github.renuevo.domain;

import com.github.renuevo.PayHomeworkV4Application;
import com.github.renuevo.domain.card.CardInfoEntity;
import com.github.renuevo.domain.card.CardInfoRepository;
import com.github.renuevo.domain.payment.*;
import com.github.renuevo.service.PaymentService;
import com.github.renuevo.web.dto.PaymentDto;
import com.github.renuevo.web.dto.PaymentResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <pre>
 * @className : RepositoryTest
 * @author : Deokhwa.Kim
 * @since : 2020-05-05
 * </pre>
 */
@ExtendWith(SpringExtension.class)
@DisplayName("데이터 DB 접근 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PayHomeworkV4Application.class)
public class RepositoryTest {

    @Autowired
    CardInfoRepository cardInfoRepository;

    @Autowired
    PaymentInstanceRepository paymentInstanceRepository;

    @Autowired
    PaymentDetailRepository paymentDetailRepository;

    @Autowired
    PaymentViewRepository paymentViewRepository;

    @Autowired
    PaymentService paymentService;

    @Test
    @DisplayName("결제 정보 저장 테스트 진행")
    public void 결제_SAVE_테스트() {

        //given
        String cardInfo = "_446PAYMENT___XXXXXXXXXXXXXXXXXXXX";
        String identityNumber = "Test Identity";

        //카드정보
        CardInfoEntity cardInfoEntity = CardInfoEntity.builder()
                .key(null)
                .useStatus(false)
                .cardInfo(cardInfo)
                .cardNumber("########")
                .build();

        //결제
        PaymentInstanceEntity paymentInstanceEntity = PaymentInstanceEntity.builder()
                .key(null)
                .identityNumber(identityNumber)
                .cardInfo(cardInfo)
                .salt("salt")
                .cancelIdentityNumber("test_cancel")
                .installment(0)
                .price(1243251)
                .tax(10)
                .build();

        //결제 내역
        PaymentDetailEntity paymentDetailEntity = PaymentDetailEntity.builder()
                .key(null)
                .identityNumber(identityNumber)
                .paymentType(PaymentActionType.PAYMENT.name())
                .installment(0)
                .price(1243251)
                .tax(10)
                .build();


        //카드 저장
        Mono<CardInfoEntity> cardInfoEntityMono = cardInfoRepository.save(cardInfoEntity);

        //결제 저장
        Mono<PaymentInstanceEntity> paymentInstanceEntityMono = paymentInstanceRepository.save(paymentInstanceEntity);

        //결제 내역 저장
        Mono<PaymentDetailEntity> paymentDetailEntityMono = paymentDetailRepository.save(paymentDetailEntity);


        //when & then
        //Card Repository Test
        StepVerifier.
                create(cardInfoEntityMono)
                .assertNext(cardInfoEntitySave -> assertNotNull(cardInfoEntitySave.getKey()))
                .thenAwait()
                .expectComplete()
                .verify();

        //Payment Repository Test
        StepVerifier.
                create(paymentInstanceEntityMono)
                .assertNext(paymentInstanceEntitySave -> assertNotNull(paymentInstanceEntitySave.getKey()))
                .thenAwait()
                .expectComplete()
                .verify();


        StepVerifier.
                create(paymentDetailEntityMono)
                .assertNext(paymentDetailEntitySave -> assertNotNull(paymentDetailEntitySave.getKey()))
                .expectComplete()
                .verify();

    }

    @Test
    @DisplayName("조회 테스트 진행")
    public void 조회_테스트() {

        //given
        PaymentDto paymentDto = PaymentDto.builder()
                .cardNumber("1234567890123456")
                .installment(4)
                .cvc(777)
                .tax(100)
                .price(10000)
                .validityRange("1125")
                .build();
        PaymentResponseDto paymentResponseDto = paymentService.paymentSave(paymentDto).block();

        Assertions.assertNotNull(paymentResponseDto);
        Mono<PaymentViewEntity> paymentViewEntityMono = paymentViewRepository.findByIdentityNumberSearch(paymentResponseDto.getIdentityNumber());

        //when & //then
        StepVerifier
                .create(paymentViewEntityMono)
                .assertNext(paymentViewEntity -> assertNotNull(paymentViewEntity.getIdentityNumber()))
                .expectComplete()
                .verify();

    }

}
