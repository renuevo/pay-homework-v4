package com.github.renuevo.service;

import com.github.renuevo.domain.PaymentActionType;
import com.github.renuevo.domain.PaymentComponent;
import com.github.renuevo.domain.card.CardDataEntity;
import com.github.renuevo.domain.card.CardDataRepository;
import com.github.renuevo.domain.payment.PaymentDetailEntity;
import com.github.renuevo.domain.payment.PaymentDetailRepository;
import com.github.renuevo.domain.payment.PaymentInstanceEntity;
import com.github.renuevo.domain.payment.PaymentInstanceRepository;
import com.github.renuevo.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


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

    private final PaymentComponent paymentComponent;
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Mono<PaymentResponseDto> paymentCall(PaymentDto paymentDto) {

        //카드 결제
        return paymentInstanceRepository.save(PaymentInstanceEntity.builder()
                .key(null)  //신규 결제
                .tax(paymentDto.getTax())
                .price(paymentDto.getPrice())
                .installment(paymentDto.getInstallment())
                .salt(paymentComponent.getSalt())                           //암호 salt
                .cardInfo(paymentComponent.getCardEncrypt(paymentDto))      //카드 정보 암호화
                .identityNumber(paymentComponent.getIdentityNumber())       //관리번호 생성
                .cancelIdentityNumber(paymentComponent.getIdentityNumber()) //전체 취소 관리번호 생성
                .build())
                .zipWhen(paymentInstanceEntity ->
                        Mono.zip(
                                paymentDetailRepository.save(PaymentDetailEntity.builder()    //결제내역
                                        .key(null)  //신규 생성
                                        .installment(paymentDto.getInstallment())
                                        .paymentType(PaymentActionType.PAYMENT.name())
                                        .identityNumber(paymentInstanceEntity.getIdentityNumber())
                                        .price(paymentDto.getPrice())
                                        .tax(paymentDto.getTax()).build()).subscribeOn(Schedulers.elastic()),

                                cardDataRepository.save(CardDataEntity.builder()              //카드사 통신
                                        .key(null)
                                        .paymentInfo(paymentComponent.getPaymentInfo(paymentDto, PaymentActionType.PAYMENT, paymentInstanceEntity.getIdentityNumber()))
                                        .build())).subscribeOn(Schedulers.elastic())
                )
                .doOnError(e -> log.error("Error Payment {} ", e.getMessage(), e))  //Error 확인
                .flatMap(tuple -> Mono.just(PaymentResponseDto.builder()                                        //Response Data Setting
                        .identityNumber(tuple.getT1().getIdentityNumber())
                        .price(tuple.getT1().getPrice())
                        .tax(tuple.getT1().getTax())
                        .installment(tuple.getT1().getInstallment())
                        .createDt(tuple.getT1().getCreateDt())
                        .build())).subscribeOn(Schedulers.elastic());
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Mono<PaymentCancelResponseDto> paymentCancel(PaymentCancelDto paymentCancelDto) {
        return paymentInstanceRepository
                .findByIdentityNumber(paymentCancelDto.getIdentityNumber())
                .zipWhen(paymentInstanceEntity -> {

                    try {

                        //부가가치세가 취소금액보다 많음
                        if (paymentInstanceEntity.getTax() < paymentCancelDto.getTax()) ;

                        //결제 금액보다 취소금액이 많음
                        if (paymentInstanceEntity.getPrice() < paymentCancelDto.getPrice()) ;

                        //전체 취소 여부 확인
                        if (paymentInstanceEntity.getCancel()) ;


                        paymentInstanceEntity.setPrice(paymentInstanceEntity.getPrice() - paymentCancelDto.getPrice());
                        paymentInstanceEntity.setTax(paymentInstanceEntity.getTax() - paymentCancelDto.getTax());

                        PaymentActionType paymentActionType = PaymentActionType.CANCEL;

                        if (paymentInstanceEntity.getPrice() == 0 && paymentInstanceEntity.getTax() == 0) {
                            paymentInstanceEntity.setCancel(true);      //전체 취소 여부
                        } else {
                            paymentActionType = PaymentActionType.PARTCANCEL;  //부분 취소
                        }

                        //카드정보 복호화
                        CardInfoDto cardInfoDto = paymentComponent.getCardDecrypt(paymentInstanceEntity.getCardInfo(), paymentInstanceEntity.getSalt());

                        return Mono.zip(
                                paymentInstanceRepository.save(paymentInstanceEntity).subscribeOn(Schedulers.elastic()),         //취소 결제
                                paymentDetailRepository.save(PaymentDetailEntity.builder()    //취소 결제 내역
                                        .key(null)  //신규 생성
                                        .installment(0)
                                        .paymentType(paymentActionType.name())
                                        .identityNumber(paymentInstanceEntity.getIdentityNumber())
                                        .price(paymentCancelDto.getPrice())
                                        .tax(paymentCancelDto.getTax()).build()).subscribeOn(Schedulers.elastic()),
                                cardDataRepository.save(CardDataEntity.builder()              //카드사 통신
                                        .key(null)
                                        .paymentInfo(paymentComponent.getCancelPaymentInfo(paymentCancelDto, cardInfoDto, paymentActionType, paymentInstanceEntity.getIdentityNumber(), paymentInstanceEntity.getCancelIdentityNumber()))
                                        .build()).subscribeOn(Schedulers.elastic())
                        );
                    } catch (Exception e) {
                        //에러 정의
                    }
                    return Mono.just(new Exception());  //에러 정의
                }).doOnError(e -> log.error("Error Payment {} ", e.getMessage(), e))  //Error 확인
                .flatMap(tuple -> Mono.just(PaymentCancelResponseDto.builder()                                        //Response Data Setting
                        .identityNumber(tuple.getT1().getIdentityNumber())
                        .price(tuple.getT1().getPrice())
                        .tax(tuple.getT1().getTax())
                        .installment(tuple.getT1().getInstallment())
                        .build())).subscribeOn(Schedulers.elastic());
    }

    @Transactional
    public Mono<?> getPaymentInfo(String identityNumber) {

        //여기서 두번 조회가 생긴당...
        return paymentInstanceRepository
                .findByIdentityNumber(identityNumber)
                .flatMap(paymentInstanceEntity -> {
                    try {
                        //카드정보 복호화
                        CardInfoDto cardInfoDto = paymentComponent.getCardDecrypt(paymentInstanceEntity.getCardInfo(), paymentInstanceEntity.getSalt());

                    } catch (Exception e) {

                    }
                    return Mono.just(new Exception());  //에러 정의
                });
    }
}
