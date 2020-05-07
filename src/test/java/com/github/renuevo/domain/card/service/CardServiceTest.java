package com.github.renuevo.domain.card.service;

import com.github.renuevo.domain.card.dao.CardCompanyEntity;
import com.github.renuevo.domain.card.dao.CardCompanyRepository;
import com.github.renuevo.domain.card.dao.CardInfoEntity;
import com.github.renuevo.domain.card.dao.CardInfoRepository;
import com.github.renuevo.domain.card.exception.CardUseException;
import com.github.renuevo.domain.card.module.CardComponent;
import com.github.renuevo.domain.payment.common.PaymentActionType;
import com.github.renuevo.domain.payment.dto.PaymentCancelDto;
import com.github.renuevo.setup.CardCompanyEntityBuilder;
import com.github.renuevo.setup.CardInfoEntityBuilder;
import com.github.renuevo.setup.PaymentDtoBuilder;
import com.github.renuevo.setup.PaymentInstanceEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * <pre>
 * @className : CardServiceTest
 * @author : Deokhwa.Kim
 * @since : 2020-05-07
 * </pre>
 */
@ExtendWith(SpringExtension.class)
class CardServiceTest {

    @Mock
    private CardInfoRepository cardInfoRepository;

    @Mock
    private CardCompanyRepository cardCompanyRepository;

    @Mock
    private CardComponent cardComponent;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    public void setUp() {
        // cardInfoEntity = CardInfoEntityBuilder.build();
    }

    @Test
    public void 카드_사용불가_확인() {

        //given
        CardInfoEntity cardInfoEntity = CardInfoEntityBuilder.usebuild();
        Mockito.when(cardInfoRepository.findByCardNumber(anyString())).thenReturn(Mono.just(cardInfoEntity));
        Mockito.when(cardComponent.getCardNumberHash(anyString())).thenReturn(cardInfoEntity.getCardInfo());

        //when
        final Mono<CardInfoEntity> cardInfoEntityMono = cardService.duplicationCardUseCheck(PaymentDtoBuilder.build());


        //then
        StepVerifier
                .create(cardInfoEntityMono)
                .verifyErrorMatches(e -> e instanceof CardUseException);

    }

    @Test
    public void 카드_사용가능_확인() {

        //given
        CardInfoEntity cardInfoEntity = CardInfoEntityBuilder.build();
        Mockito.when(cardInfoRepository.findByCardNumber(anyString())).thenReturn(Mono.just(cardInfoEntity));
        Mockito.when(cardComponent.getCardNumberHash(anyString())).thenReturn(cardInfoEntity.getCardInfo());

        //when
        final Mono<CardInfoEntity> cardInfoEntityMono = cardService.duplicationCardUseCheck(PaymentDtoBuilder.build());


        //then
        StepVerifier
                .create(cardInfoEntityMono)
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();

    }

    @Test
    public void 카드_취소_저장_통신() {

        //given
        CardCompanyEntity cardCompanyEntity = CardCompanyEntityBuilder.newBuild();
        Mockito.when(cardCompanyRepository.save(any(CardCompanyEntity.class))).thenReturn(Mono.just(cardCompanyEntity));

        //when
        final Mono<CardCompanyEntity> cardCompanyEntityMono = cardService.cardCancelSave(new PaymentCancelDto(), PaymentInstanceEntityBuilder.build());

        //then
        StepVerifier
                .create(cardCompanyEntityMono)
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();

    }

    @Test
    public void 카드_결제_저장_통신() {

        //given
        CardCompanyEntity cardCompanyEntity = CardCompanyEntityBuilder.newBuild();
        Mockito.when(cardCompanyRepository.save(any(CardCompanyEntity.class))).thenReturn(Mono.just(cardCompanyEntity));

        //when
        final Mono<CardCompanyEntity> cardCompanyEntityMono = cardService.cardPaymentSave(PaymentDtoBuilder.build(), PaymentActionType.PAYMENT, PaymentInstanceEntityBuilder.build());

        //then
        StepVerifier
                .create(cardCompanyEntityMono)
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();

    }

}