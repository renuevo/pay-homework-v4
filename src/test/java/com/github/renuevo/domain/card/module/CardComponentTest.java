package com.github.renuevo.domain.card.module;

import com.github.renuevo.domain.card.dao.CardInfoEntity;
import com.github.renuevo.domain.payment.common.PaymentActionType;
import com.github.renuevo.domain.payment.dao.PaymentInstanceEntity;
import com.github.renuevo.domain.payment.dto.PaymentDto;
import com.github.renuevo.setup.CardInfoEntityBuilder;
import com.github.renuevo.setup.PaymentDtoBuilder;
import com.github.renuevo.setup.PaymentInstanceEntityBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class CardComponentTest {

    @InjectMocks
    CardComponent cardComponent;

    @Test
    public void 카드_통신_값_생성() {

        //given
        PaymentDto paymentDto = PaymentDtoBuilder.build();
        PaymentInstanceEntity paymentInstanceEntity = PaymentInstanceEntityBuilder.build();
        CardInfoEntity cardInfoEntity = CardInfoEntityBuilder.build();

        ///when
        String paymentInfo = cardComponent.getPaymentInfo(paymentDto, PaymentActionType.PAYMENT, cardInfoEntity.getCardInfo(), paymentInstanceEntity.getIdentityNumber());

        //then
        Assertions.assertEquals(paymentInfo,
                " 446PAYMENT   " +
                        "2b44fddfa091bc5c12d01234567890123456    " +
                        "001125777    " +
                        "3000000000030000                    " +
                        "c75d2519eba6b3b99495547074bc7f6fb1541375261e0f2d68db714cbc1312bb18ce41313e18ab0611bf5679a7b4b279" +
                        "                                                                                                   " +
                        "                                                                                                      " +
                        "                                                  "
        );

    }

}
