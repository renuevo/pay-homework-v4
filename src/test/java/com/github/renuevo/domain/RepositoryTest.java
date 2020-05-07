package com.github.renuevo.domain;

import com.github.renuevo.PayHomeworkV4Application;
import com.github.renuevo.domain.card.dao.CardInfoEntity;
import com.github.renuevo.domain.card.dao.CardInfoRepository;
import com.github.renuevo.domain.payment.dao.*;
import com.github.renuevo.domain.payment.dto.PaymentResponseDto;
import com.github.renuevo.service.PaymentWebService;
import com.github.renuevo.setup.CardInfoEntityBuilder;
import com.github.renuevo.setup.PaymentDetailEntityBuilder;
import com.github.renuevo.setup.PaymentDtoBuilder;
import com.github.renuevo.setup.PaymentInstanceEntityBuilder;
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
    PaymentWebService paymentWebService;

    @Test
    @DisplayName("결제 정보 저장 테스트 진행")
    public void 결제_SAVE_테스트() {

        //when
        //카드 저장
        Mono<CardInfoEntity> cardInfoEntityMono = cardInfoRepository.save(CardInfoEntityBuilder.newBuild());

        //결제 저장
        Mono<PaymentInstanceEntity> paymentInstanceEntityMono = paymentInstanceRepository.save(PaymentInstanceEntityBuilder.newBuild());

        //결제 내역 저장
        Mono<PaymentDetailEntity> paymentDetailEntityMono = paymentDetailRepository.save(PaymentDetailEntityBuilder.newPaymentBuild());


        //then
        //Card Repository Test
        StepVerifier.
                create(cardInfoEntityMono)
                .assertNext(cardInfoEntitySave -> assertNotNull(cardInfoEntitySave.getKey()))
                .thenAwait()
                .verifyComplete();

        //Payment Repository Test
        StepVerifier.
                create(paymentInstanceEntityMono)
                .assertNext(paymentInstanceEntitySave -> assertNotNull(paymentInstanceEntitySave.getKey()))
                .thenAwait()
                .verifyComplete();

        //PaymentDetail Repository Test
        StepVerifier.
                create(paymentDetailEntityMono)
                .assertNext(paymentDetailEntitySave -> assertNotNull(paymentDetailEntitySave.getKey()))
                .verifyComplete();

    }

    @Test
    @DisplayName("조회 테스트 진행")
    public void 조회_테스트() {

        //given
        PaymentResponseDto paymentResponseDto = paymentWebService.paymentSave(PaymentDtoBuilder.build()).block();

        //when
        assert paymentResponseDto != null;
        Mono<PaymentViewEntity> paymentViewEntityMono = paymentViewRepository.findByIdentityNumberSearch(paymentResponseDto.getIdentityNumber());

        //then
        StepVerifier
                .create(paymentViewEntityMono)
                .assertNext(paymentViewEntity -> assertNotNull(paymentViewEntity.getIdentityNumber()))
                .verifyComplete();

    }

}
