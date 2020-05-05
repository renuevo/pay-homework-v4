package com.github.renuevo.service;

import com.github.renuevo.domain.PaymentActionType;
import com.github.renuevo.domain.PaymentComponent;
import com.github.renuevo.domain.card.CardCompanyEntity;
import com.github.renuevo.domain.card.CardCompanyRepository;
import com.github.renuevo.domain.card.CardInfoEntity;
import com.github.renuevo.domain.card.CardInfoRepository;
import com.github.renuevo.domain.payment.*;
import com.github.renuevo.exception.ErrorResponse;
import com.github.renuevo.exception.service.CardUseException;
import com.github.renuevo.exception.service.PaymentCancelException;
import com.github.renuevo.exception.service.PaymentException;
import com.github.renuevo.exception.service.PaymentViewException;
import com.github.renuevo.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;
    private final PaymentComponent paymentComponent;

    private final CardInfoRepository cardInfoRepository;
    private final CardCompanyRepository cardCompanyRepository;
    private final PaymentViewRepository paymentViewRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final PaymentInstanceRepository paymentInstanceRepository;

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

        //이중 결제 확인후 결제 진행
        return duplicationPaymentCheck(paymentDto).flatMap(cardInfoEntity -> {

            //카드 결제 진행
            cardInfoEntity.setUseStatus(true);
            return cardInfoRepository.save(cardInfoEntity).flatMap(cardInfoEntity1 ->

                    //결제 현황 저장
                    paymentInstanceEntitySave(paymentDto, cardInfoEntity1)
                            .zipWhen(paymentInstanceEntity ->
                                    Mono.zip(
                                            paymentDetailEntitySave(paymentDto, paymentInstanceEntity, PaymentActionType.PAYMENT),     //결제내역 저장
                                            cardInfoEntitySave(paymentComponent.getPaymentInfo(paymentDto, PaymentActionType.PAYMENT, paymentInstanceEntity.getIdentityNumber()))           //카드사 통신
                                    ).subscribeOn(Schedulers.elastic())
                            )


                            //Response Data 생성
                            .flatMap(tuple -> Mono.just(modelMapper.map(tuple.getT1(), PaymentResponseDto.class)))
                            .subscribeOn(Schedulers.elastic()))


                    //카드 결제 완료
                    .doFinally(f -> {
                        cardInfoEntity.setUseStatus(false);
                        cardInfoRepository.save(cardInfoEntity).subscribe();
                    });
        })
                //에러 처리
                .onErrorResume(e -> Mono.error(new PaymentException(e)));
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
        return paymentInstanceRepository.findByIdentityNumber(paymentCancelDto.getIdentityNumber()) //관리번호 조회
                .switchIfEmpty(Mono.error(new PaymentCancelException(ErrorResponse.FieldError.of("identityNumber", "", "존재하지 않는 관리번호입니다"))))


                //취소 진행
                .zipWhen(paymentInstanceEntity -> {

                    //결제 금액보다 취소금액이 많음
                    if (paymentInstanceEntity.getPrice() < paymentCancelDto.getPrice())
                        return Mono.error(new PaymentCancelException(ErrorResponse.FieldError.of("price", String.valueOf(paymentInstanceEntity.getPrice()), String.format("%s 은 %s보다 작아야 합니다", paymentInstanceEntity.getPrice(), paymentCancelDto.getPrice()))));

                    //부가가치세가 취소금액보다 많음
                    if (paymentInstanceEntity.getTax() < paymentCancelDto.getTax())
                        return Mono.error(new PaymentCancelException(ErrorResponse.FieldError.of("tax", String.valueOf(paymentInstanceEntity.getTax()), String.format("%s 은 %s보다 작아야 합니다", paymentInstanceEntity.getTax(), paymentCancelDto.getTax()))));

                    //전체 취소 여부 확인
                    if (paymentInstanceEntity.getCancel())
                        return Mono.error(new PaymentCancelException(ErrorResponse.FieldError.of("cancel", "", "이미 취소된 결제 입니다")));


                    //결제 취소 남은 금액 계산
                    paymentInstanceEntity.setPrice(paymentInstanceEntity.getPrice() - paymentCancelDto.getPrice());
                    paymentInstanceEntity.setTax(paymentInstanceEntity.getTax() - paymentCancelDto.getTax());
                    if (paymentInstanceEntity.getPrice() == 0 && paymentInstanceEntity.getTax() == 0)
                        paymentInstanceEntity.setCancel(true);      //전체 취소 여부


                    //카드정보 복호화
                    CardInfoDto cardInfoDto = paymentComponent.getCardDecrypt(paymentInstanceEntity.getCardInfo(), paymentInstanceEntity.getSalt());

                    return Mono.zip(
                            paymentInstanceRepository.save(paymentInstanceEntity).subscribeOn(Schedulers.elastic()),        //취소 결제 업데이트
                            paymentDetailEntitySave(paymentCancelDto, paymentInstanceEntity, PaymentActionType.CANCEL),     //취소 결제 내역 저장
                            cardInfoEntitySave(paymentComponent.getCancelPaymentInfo(paymentCancelDto, cardInfoDto, PaymentActionType.CANCEL, paymentInstanceEntity.getCancelIdentityNumber()))           //카드사 통신
                    );
                })


                //Response Data 생성
                .flatMap(tuple -> Mono.just(modelMapper.map(tuple.getT1(), PaymentCancelResponseDto.class)))
                .onErrorResume(e -> {
                    if (e instanceof PaymentCancelException) return Mono.error(e);
                    else return Mono.error(new PaymentCancelException(e));
                })
                .subscribeOn(Schedulers.elastic());
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
        return paymentViewRepository.findByIdentityNumberSearch(identityNumber)
                .switchIfEmpty(Mono.error(new PaymentViewException(ErrorResponse.FieldError.of("identityNumber", "", "존재하지 않는 관리번호입니다"))))
                .flatMap(paymentViewEntity -> {

                    //카드정보 복호화 및 마스킹 처리
                    CardInfoDto cardInfoDto = paymentComponent.getCardDecrypt(paymentViewEntity.getCardInfo(), paymentViewEntity.getSalt());
                    cardInfoDto.setCardNumber("******" + cardInfoDto.getCardNumber().substring(6, cardInfoDto.getCardNumber().length() - 3) + "***");

                    //Response Data 생성
                    PaymentViewResponseDto paymentViewResponseDto = modelMapper.map(paymentViewEntity, PaymentViewResponseDto.class);
                    paymentViewResponseDto.setCardInfoDto(cardInfoDto);
                    return Mono.just(paymentViewResponseDto);
                })
                .onErrorResume(e -> Mono.error(new PaymentViewException(e)));

    }


    /**
     * <pre>
     *  @methodName : duplicationPaymentCheck
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 12:41
     *  @summary : 카드 이중 결제 확인
     *  @param : [paymentDto]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.card.CardInfoEntity>
     * </pre>
     */
    private Mono<CardInfoEntity> duplicationPaymentCheck(PaymentDto paymentDto) {
        return cardInfoRepository.findByCardNumber(paymentComponent.getCardNumberHash(paymentDto.getCardNumber()))   //카드 정보 조회

                //카드 정보 미존재일 경우 신규 생성
                .switchIfEmpty(
                        Mono.just(CardInfoEntity
                                .builder()
                                .key(null)  //신규 생성
                                .cardNumber(paymentComponent.getCardNumberHash(paymentDto.getCardNumber()))
                                .cardInfo(paymentComponent.getCardEncrypt(paymentDto))
                                .useStatus(false)
                                .build())
                )

                //사용 가능여부 확인
                .filter(cardInfoEntity -> !cardInfoEntity.getUseStatus())
                .switchIfEmpty(Mono.error(new CardUseException()));
    }

    /**
     * <pre>
     *  @methodName : paymentInstanceEntityMono
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 12:44
     *  @summary : 결제 현황 Insert
     *  @param : [paymentDto, cardInfoEntity]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.payment.PaymentInstanceEntity>
     * </pre>
     */
    private Mono<PaymentInstanceEntity> paymentInstanceEntitySave(PriceDto priceDto, CardInfoEntity cardInfoEntity) {
        return paymentInstanceRepository.save(PaymentInstanceEntity
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
     *  @methodName : cardInfoEntityMono
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 12:44
     *  @summary : 카드사 통신 insert
     *  @param : [paymentDto, paymentInstanceEntity]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.card.CardCompanyEntity>
     * </pre>
     */
    private Mono<CardCompanyEntity> cardInfoEntitySave(String paymentInfo) {
        return cardCompanyRepository.save(CardCompanyEntity.builder()              //카드사 통신
                .key(null)
                .paymentInfo(paymentInfo)
                .build()).subscribeOn(Schedulers.elastic());
    }

    /**
     * <pre>
     *  @methodName : paymentDetailEntityMono
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 12:44
     *  @summary : 결제 내역 insert
     *  @param : [paymentDto, paymentInstanceEntity]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.payment.PaymentDetailEntity>
     * </pre>
     */
    private Mono<PaymentDetailEntity> paymentDetailEntitySave(PriceDto priceDto, PaymentInstanceEntity paymentInstanceEntity, PaymentActionType paymentActionType) {
        return paymentDetailRepository.save(PaymentDetailEntity.builder()    //결제내역
                .key(null)  //신규 생성
                .installment(priceDto.getInstallment())
                .paymentType(paymentActionType.name())
                .identityNumber(paymentInstanceEntity.getIdentityNumber())
                .price(priceDto.getPrice())
                .tax(priceDto.getTax()).build())
                .subscribeOn(Schedulers.elastic());
    }
}
