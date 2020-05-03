package com.github.renuevo.service;

import com.github.renuevo.common.CardActionType;
import com.github.renuevo.common.DataCommon;
import com.github.renuevo.common.CardInfoUtils;
import com.github.renuevo.domain.card.CardDataEntity;
import com.github.renuevo.domain.card.CardDataRepository;
import com.github.renuevo.domain.payment.PaymentDetailEntity;
import com.github.renuevo.domain.payment.PaymentDetailRepository;
import com.github.renuevo.domain.payment.PaymentInstanceEntity;
import com.github.renuevo.domain.payment.PaymentInstanceRepository;
import com.github.renuevo.web.dto.PaymentCancelDto;
import com.github.renuevo.web.dto.PaymentDto;
import com.github.renuevo.web.dto.PaymentResponseDto;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.io.IOException;


/**
 * <pre>
 * @className : PaymentService
 * @author : Deokhwa.Kim
 * @since : 2020-05-02
 * </pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CardInfoUtils cardInfoUtils;
    private final PaymentInstanceRepository paymentInstanceRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final CardDataRepository cardDataRepository;

    /**
     * <pre>
     *  @methodName : paymentCall
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-03 오후 2:42
     *  @summary : 카드사 결제 기능
     *  @param : [paymentDto]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.web.dto.PaymentResponseDto>
     * </pre>
     */
    @Transactional
    public Mono<PaymentResponseDto> paymentCall(PaymentDto paymentDto) {

        //카드 결제
        return paymentInstanceRepository.save(PaymentInstanceEntity.builder()
                .key(null)  //신규 결제
                .tax(paymentDto.getTax())
                .price(paymentDto.getPrice())
                .installment(paymentDto.getInstallment())
                .salt(cardInfoUtils.getSalt())
                .cardInfo(cardInfoUtils.getCardEncrypt(paymentDto))
                .build())
                .zipWhen(paymentInstanceEntity ->
                        Mono.zip(
                                paymentDetailRepository.save(PaymentDetailEntity.builder()    //결제내역
                                        .key(null)  //신규 생성
                                        .installment(paymentDto.getInstallment())
                                        .paymentType(CardActionType.PAYMENT.name())
                                        .paymentKey(paymentInstanceEntity.getKey())
                                        .price(paymentDto.getPrice())
                                        .tax(paymentDto.getTax()).build()),

                                cardDataRepository.save(CardDataEntity.builder()              //카드사 통신
                                        .key(null)
                                        .paymentInfo(cardInfoUtils.getPaymentInfo(paymentDto, CardActionType.PAYMENT, paymentInstanceEntity.getKey()))
                                        .build()))
                )
                .doOnError(e -> log.error("Error Payment {} ", e.getMessage(), e))  //Error 확인
                .flatMap(tuple -> Mono.just(PaymentResponseDto.builder()                                        //Response Data Setting
                        .number(Strings.padStart(String.valueOf(tuple.getT1().getKey()), DataCommon.NUMBER, '0'))
                        .price(tuple.getT1().getPrice())
                        .tax(tuple.getT1().getTax())
                        .installment(tuple.getT1().getInstallment())
                        .createDt(tuple.getT1().getCreateDt())
                        .build()));
    }


    @Transactional
    public Mono<?> paymentCancel(PaymentCancelDto paymentCancelDto) {
        return null;
    }


}
