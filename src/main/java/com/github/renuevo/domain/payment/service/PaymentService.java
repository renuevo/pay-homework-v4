package com.github.renuevo.domain.payment.service;

import com.github.renuevo.domain.card.dao.CardInfoEntity;
import com.github.renuevo.domain.card.dto.CardInfoDto;
import com.github.renuevo.domain.payment.common.PaymentActionType;
import com.github.renuevo.domain.payment.dao.*;
import com.github.renuevo.domain.payment.dto.*;
import com.github.renuevo.domain.payment.exception.PaymentCancelException;
import com.github.renuevo.domain.payment.module.PaymentComponent;
import com.github.renuevo.grobal.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * <pre>
 * @className : PaymentService
 * @author : Deokhwa.Kim
 * @since : 2020-05-07
 * @summary : 결제 서비스
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final ModelMapper modelMapper;
    private final PaymentComponent paymentComponent;
    private final PaymentViewRepository paymentViewRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final PaymentInstanceRepository paymentInstanceRepository;



    /* 결제 정보 생성 */

    /**
     * <pre>
     *  @methodName : paymentInstanceEntityMono
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 12:44
     *  @summary : 결제 정보 생성 Insert
     *  @param : [paymentDto, cardInfoEntity]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.payment.PaymentInstanceEntity>
     * </pre>
     */
    @Transactional
    public Mono<PaymentInstanceEntity> paymentInstanceEntitySave(PriceDto priceDto, CardInfoEntity cardInfoEntity) {
        return paymentInstanceEntitySave(PaymentInstanceEntity
                .builder()
                .key(null)  //신규 결제
                .tax(priceDto.getTax())
                .price(priceDto.getPrice())
                .installment(priceDto.getInstallment())
                .cardInfo(cardInfoEntity.getCardInfo())      //카드 정보 암호화
                .salt(paymentComponent.getSalt())                           //암호 salt
                .identityNumber(paymentComponent.getIdentityNumber())       //관리번호 생성
                .cancelIdentityNumber(paymentComponent.getIdentityNumber()) //전체 취소 관리번호 생성
                .build());
    }

    /**
     * <pre>
     *  @methodName : paymentInstanceEntitySave
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-07 오후 1:06
     *  @summary : 결제 DB insert
     *  @param : [paymentInstanceEntity]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.payment.dao.PaymentInstanceEntity>
     * </pre>
     */
    @Transactional
    public Mono<PaymentInstanceEntity> paymentInstanceEntitySave(PaymentInstanceEntity paymentInstanceEntity) {
        return paymentInstanceRepository.save(paymentInstanceEntity).subscribeOn(Schedulers.elastic());
    }

    /**
     * <pre>
     *  @methodName : paymentDetailEntitySave
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-07 오후 1:06
     *  @summary : 결제 내역 DB Insert
     *  @param : [priceDto, paymentInstanceEntity, paymentActionType]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.payment.dao.PaymentDetailEntity>
     * </pre>
     */
    @Transactional
    public Mono<PaymentDetailEntity> paymentDetailEntitySave(PriceDto priceDto, PaymentInstanceEntity paymentInstanceEntity, PaymentActionType paymentActionType) {
        return paymentDetailRepository.save(PaymentDetailEntity.builder()    //결제내역
                .key(null)  //신규 생성
                .installment(priceDto.getInstallment())
                .paymentType(paymentActionType.name())
                .identityNumber(paymentInstanceEntity.getIdentityNumber())
                .price(priceDto.getPrice())
                .tax(priceDto.getTax()).build())
                .subscribeOn(Schedulers.elastic());
    }



    /* 결제 조회 */

    /**
     * <pre>
     *  @methodName : findPaymentInstance
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-07 오후 1:07
     *  @summary : 결제 조회
     *  @param : [identityNumber]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.payment.dao.PaymentInstanceEntity>
     * </pre>
     */
    @Transactional
    public Mono<PaymentInstanceEntity> findPaymentInstance(String identityNumber) {
        return paymentInstanceRepository.findByIdentityNumber(identityNumber)
                .switchIfEmpty(Mono.error(new PaymentCancelException(ErrorResponse.FieldError.of("identityNumber", "", "존재하지 않는 관리번호입니다"))));
    }

    /**
     * <pre>
     *  @methodName : findPaymentView
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-07 오후 1:07
     *  @summary : 결제 내역 조회
     *  @param : [identityNumber]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.payment.dao.PaymentViewEntity>
     * </pre>
     */
    @Transactional
    public Mono<PaymentViewEntity> findPaymentView(String identityNumber) {
        return paymentViewRepository.findByIdentityNumberSearch(identityNumber)
                .switchIfEmpty(Mono.error(new PaymentCancelException(ErrorResponse.FieldError.of("identityNumber", "", "존재하지 않는 관리번호입니다"))));
    }


    /* 결제 취소 데이터 생성 데이터 생성*/

    /**
     * <pre>
     *  @methodName : cancelPayment
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-07 오전 12:26
     *  @summary : 결제 취소 데이터 생성
     *  @param : [paymentInstanceEntity, paymentCancelDto]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.payment.PaymentInstanceEntity>
     * </pre>
     */
    public Mono<Tuple2<PaymentInstanceEntity, PaymentCancelDto>> cancelPayment(PaymentInstanceEntity paymentInstanceEntity, PaymentCancelDto paymentCancelDto) {

        //결제 취소 남은 금액 계산
        paymentInstanceEntity.setPrice(paymentInstanceEntity.getPrice() - paymentCancelDto.getPrice());

        //자동계산 결제 취소 성공
        if (paymentInstanceEntity.getPrice() == 0 && paymentCancelDto.taxCheck() && paymentInstanceEntity.getTax() < paymentCancelDto.getTax())
            paymentCancelDto.setTax(paymentInstanceEntity.getTax());

        //부가가치세 계산
        paymentInstanceEntity.setTax(paymentInstanceEntity.getTax() - paymentCancelDto.getTax());

        //전체 취소 여부
        if (paymentInstanceEntity.getPrice() == 0 && paymentInstanceEntity.getTax() == 0)
            paymentInstanceEntity.setCancel(true);

        return Mono.just(Tuples.of(paymentInstanceEntity, paymentCancelDto));
    }



    /* Response 데이터 생성*/

    /**
     * <pre>
     *  @methodName : createPaymentResponse
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-07 오후 1:08
     *  @summary : 결제 반환 데이터 생성
     *  @param : [paymentInstanceEntity]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.payment.dto.PaymentResponseDto>
     * </pre>
     */
    public Mono<PaymentResponseDto> createPaymentResponse(PaymentInstanceEntity paymentInstanceEntity) {
        return Mono.just(modelMapper.map(paymentInstanceEntity, PaymentResponseDto.class));
    }

    /**
     * <pre>
     *  @methodName : createPaymentCancelResponse
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-07 오후 1:08
     *  @summary : 결제 취소 반환 데이터 생성
     *  @param : [paymentInstanceEntity]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.payment.dto.PaymentCancelResponseDto>
     * </pre>
     */
    public Mono<PaymentCancelResponseDto> createPaymentCancelResponse(PaymentInstanceEntity paymentInstanceEntity) {
        return Mono.just(modelMapper.map(paymentInstanceEntity, PaymentCancelResponseDto.class));
    }

    /**
     * <pre>
     *  @methodName : createPaymentViewResponse
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-07 오후 1:09
     *  @summary : 결제 내역 반환 데이터 생성
     *  @param : [paymentViewEntity, cardInfoDto]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.payment.dto.PaymentViewResponseDto>
     * </pre>
     */
    public Mono<PaymentViewResponseDto> createPaymentViewResponse(PaymentViewEntity paymentViewEntity, CardInfoDto cardInfoDto) {

        //카드정보 복호화 및 마스킹 처리
        cardInfoDto.setCardNumber("******" + cardInfoDto.getCardNumber().substring(6, cardInfoDto.getCardNumber().length() - 3) + "***");

        //Response Data 생성
        PaymentViewResponseDto paymentViewResponseDto = modelMapper.map(paymentViewEntity, PaymentViewResponseDto.class);
        paymentViewResponseDto.setCardInfoDto(cardInfoDto);
        return Mono.just(paymentViewResponseDto);
    }

}