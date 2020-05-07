package com.github.renuevo.service;

import com.github.renuevo.domain.card.dao.CardCompanyEntity;
import com.github.renuevo.domain.card.service.CardService;
import com.github.renuevo.domain.payment.common.PaymentActionType;
import com.github.renuevo.domain.payment.dao.PaymentDetailEntity;
import com.github.renuevo.domain.payment.dao.PaymentInstanceEntity;
import com.github.renuevo.domain.payment.dto.*;
import com.github.renuevo.domain.payment.exception.PaymentCancelException;
import com.github.renuevo.domain.payment.exception.PaymentException;
import com.github.renuevo.domain.payment.exception.PaymentViewException;
import com.github.renuevo.domain.payment.module.PaymentValidation;
import com.github.renuevo.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;


/**
 * <pre>
 * @className : PaymentWebService
 * @author : Deokhwa.Kim
 * @since : 2020-05-02
 * </pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentWebService {

    private final CardService cardService;
    private final PaymentService paymentService;


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
    public Mono<PaymentResponseDto> paymentSave(PaymentDto paymentDto) {
        return cardService.duplicationCardUseCheck(paymentDto)   //이중 결제 확인후 결제 진행
                .flatMap(cardInfoEntity -> cardService.useCardInfoEntity(cardInfoEntity)    //카드 결제 진행
                        .flatMap(useCardInfoEntity -> paymentService.paymentInstanceEntitySave(paymentDto, useCardInfoEntity))  //결제 현황 저장 
                        .zipWhen(paymentInstanceEntity -> paymentMultiProcess(paymentInstanceEntity, paymentDto))   //결제 프로세스 처리
                        .flatMap(tuple -> paymentService.createPaymentResponse(tuple.getT1()))  //Response Data 생성
                        .doFinally(f -> cardService.cardUseDone(cardInfoEntity).subscribe()))
                .onErrorResume(e -> Mono.error(new PaymentException(e)))
                .subscribeOn(Schedulers.elastic());
    }

    /**
     * <pre>
     *  @methodName : paymentCancel
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 2:06
     *  @summary : 결제 취소
     *  @param : [paymentCancelDto]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.web.dto.PaymentCancelResponseDto>
     * </pre>
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Mono<PaymentCancelResponseDto> paymentCancel(PaymentCancelDto paymentCancelDto) {
        return paymentService.findPaymentInstance(paymentCancelDto.getIdentityNumber()) //관리번호 조회
                .filter(paymentInstanceEntity -> PaymentValidation.paymentCancelValidation(paymentInstanceEntity, paymentCancelDto))   //취소 validation check
                .flatMap(paymentInstanceEntity -> paymentService.cancelPayment(paymentInstanceEntity, paymentCancelDto))   //결제 취소 데이터 생성
                .zipWhen(tuple -> paymentCancelMultiProcess(tuple.getT1(), tuple.getT2()))
                .flatMap(tuple -> paymentService.createPaymentCancelResponse(tuple.getT1().getT1()))              //Response Data 생성
                .onErrorResume(e -> {
                    if (e instanceof PaymentCancelException) return Mono.error(e);
                    else return Mono.error(new PaymentCancelException(e));
                }).subscribeOn(Schedulers.elastic());
    }

    /**
     * <pre>
     *  @methodName : getPaymentInfo
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 3:22
     *  @summary : 결제 정보 조회
     *  @param : [identityNumber]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.web.dto.PaymentViewResponseDto>
     * </pre>
     */
    @Transactional(readOnly = true)
    public Mono<PaymentViewResponseDto> getPaymentInfo(String identityNumber) {
        return paymentService.findPaymentView(identityNumber)
                .flatMap(paymentViewEntity -> paymentService.createPaymentViewResponse(paymentViewEntity, cardService.getCardDecrypt(paymentViewEntity.getCardInfo(), paymentViewEntity.getSalt())))
                .onErrorResume(e -> {
                    if (e instanceof PaymentViewException) return Mono.error(e);
                    else return Mono.error(new PaymentViewException(e));
                }).subscribeOn(Schedulers.elastic());
    }


    /*이 이후 부터 결제 프로세스*/

    /**
     * <pre>
     *  @methodName : paymentMultiProcess
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-06 오후 7:08
     *  @summary : 결제 관련 프로세스 진행 -> zip 구조로 자유롭게 추가 가능
     *  @param : [paymentInstanceEntity, paymentDto]
     *  @return : reactor.core.publisher.Mono<reactor.util.function.Tuple2<com.github.renuevo.domain.payment.PaymentDetailEntity,com.github.renuevo.domain.card.CardCompanyEntity>>
     * </pre>
     */
    @Transactional
    public Mono<Tuple2<PaymentDetailEntity, CardCompanyEntity>> paymentMultiProcess(PaymentInstanceEntity paymentInstanceEntity, PaymentDto paymentDto) {
        return Mono.zip(
                paymentService.paymentDetailEntitySave(paymentDto, paymentInstanceEntity, PaymentActionType.PAYMENT),     //결제내역 저장
                cardService.cardPaymentSave(paymentDto, PaymentActionType.PAYMENT, paymentInstanceEntity)           //카드사 통신
        ).subscribeOn(Schedulers.elastic());   //결제 프로세스
    }

    /**
     * <pre>
     *  @methodName : paymentMultiProcess
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-06 오후 7:08
     *  @summary : 결제 관련 프로세스 진행 -> zip 구조로 자유롭게 추가 가능
     *  @param : [paymentInstanceEntity, paymentDto]
     *  @return : reactor.core.publisher.Mono<reactor.util.function.Tuple2<com.github.renuevo.domain.payment.PaymentDetailEntity,com.github.renuevo.domain.card.CardCompanyEntity>>
     * </pre>
     */
    @Transactional
    public Mono<Tuple3<PaymentInstanceEntity, PaymentDetailEntity, CardCompanyEntity>> paymentCancelMultiProcess(PaymentInstanceEntity paymentInstanceEntity, PaymentCancelDto paymentCancelDto) {
        return Mono.zip(
                paymentService.paymentInstanceEntitySave(paymentInstanceEntity),        //취소 결제 업데이트
                paymentService.paymentDetailEntitySave(paymentCancelDto, paymentInstanceEntity, PaymentActionType.CANCEL),     //취소 결제 내역 저장
                cardService.cardCancelSave(paymentCancelDto, paymentInstanceEntity)          //카드사 통신
        ).subscribeOn(Schedulers.elastic());   //결제 프로세스
    }
}
