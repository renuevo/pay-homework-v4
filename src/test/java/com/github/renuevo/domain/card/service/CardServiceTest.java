package com.github.renuevo.domain.card.service;

import com.github.renuevo.domain.card.dao.CardCompanyRepository;
import com.github.renuevo.domain.card.dao.CardInfoEntity;
import com.github.renuevo.domain.card.dao.CardInfoRepository;
import com.github.renuevo.domain.card.exception.CardUseException;
import com.github.renuevo.domain.card.module.CardComponent;
import com.github.renuevo.setup.CardInfoEntityBuilder;
import com.github.renuevo.setup.PaymentDtoBuilder;
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

import static org.mockito.ArgumentMatchers.anyString;

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
    void 카드_사용불가_확인() {

        //given
        CardInfoEntity cardInfoEntity = CardInfoEntityBuilder.usebuild();
        Mockito.when(cardInfoRepository.findByCardNumber(anyString())).thenReturn(Mono.just(cardInfoEntity));
        Mockito.when(cardComponent.getCardNumberHash(anyString())).thenReturn(cardInfoEntity.getCardInfo());

        //when
        final Mono<CardInfoEntity> cardInfoEntityMono = cardService.duplicationCardUseCheck(PaymentDtoBuilder.build());


        //then
        StepVerifier
                .create(cardInfoEntityMono)
                .verifyErrorMatches(e -> e instanceof CardUseException);    //카드 사용 에러 확인

    }

    @Test
    void 카드_사용가능_확인() {

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
    void cardPaymentSave() {

    }

    @Test
    void useCardInfoEntity() {

    }

    @Test
    void cardCancelSave() {

    }

    @Test
    void cardUseDone() {

    }

    @Test
    void getCardDecrypt() {

    }
}