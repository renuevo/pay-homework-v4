package com.github.renuevo.domain.payment.service;

import com.github.renuevo.PayHomeworkV4Application;
import com.github.renuevo.domain.card.dto.CardInfoDto;
import com.github.renuevo.domain.payment.dao.PaymentInstanceEntity;
import com.github.renuevo.domain.payment.dao.PaymentViewEntity;
import com.github.renuevo.domain.payment.dto.PaymentCancelDto;
import com.github.renuevo.domain.payment.dto.PaymentViewResponseDto;
import com.github.renuevo.setup.PaymentCancelDtoBuilder;
import com.github.renuevo.setup.PaymentInstanceEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

/**
 * <pre>
 * @className : PaymentServiceTest
 * @author : Deokhwa.Kim
 * @since : 2020-05-07
 * </pre>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PayHomeworkV4Application.class)
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    public void 카드_마스킹_처리() {

        //given
        String cardMasking = "******78***";
        CardInfoDto cardInfoDto = new CardInfoDto(cardMasking, "1125", 777);

        //when
        Mono<PaymentViewResponseDto> paymentViewResponseDtoMono = paymentService.createPaymentViewResponse(new PaymentViewEntity(), cardInfoDto);

        //then
        StepVerifier
                .create(paymentViewResponseDtoMono)
                .assertNext(data -> Assertions.assertEquals(data.getCardInfoDto().getCardNumber(), cardMasking))
                .verifyComplete();

    }

    @Test
    public void 결제_취소_금액_테스트() {

        //given
        PaymentInstanceEntity paymentInstanceEntity = PaymentInstanceEntityBuilder.build();
        PaymentCancelDto paymentCancelDto = PaymentCancelDtoBuilder.build();
        int price = paymentInstanceEntity.getPrice() - paymentCancelDto.getPrice();

        //when
        Mono<Tuple2<PaymentInstanceEntity, PaymentCancelDto>> panymentCancelMono = paymentService.cancelPayment(paymentInstanceEntity, paymentCancelDto);

        //then
        StepVerifier
                .create(panymentCancelMono)
                .assertNext(tuple -> Assertions.assertEquals(tuple.getT1().getPrice(), price))
                .verifyComplete();

    }

}
