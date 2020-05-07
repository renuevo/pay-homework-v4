package com.github.renuevo.domain.card.service;

import com.github.renuevo.domain.card.dao.CardCompanyEntity;
import com.github.renuevo.domain.card.dao.CardCompanyRepository;
import com.github.renuevo.domain.card.dao.CardInfoEntity;
import com.github.renuevo.domain.card.dao.CardInfoRepository;
import com.github.renuevo.domain.card.dto.CardInfoDto;
import com.github.renuevo.domain.card.exception.CardUseException;
import com.github.renuevo.domain.card.module.CardComponent;
import com.github.renuevo.domain.payment.common.PaymentActionType;
import com.github.renuevo.domain.payment.dao.PaymentInstanceEntity;
import com.github.renuevo.domain.payment.dto.PaymentCancelDto;
import com.github.renuevo.domain.payment.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * <pre>
 * @className : CardService
 * @author : Deokhwa.Kim
 * @since : 2020-05-07
 * @summary : 카드 서비스
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class CardService {

    private final CardComponent cardComponent;
    private final CardInfoRepository cardInfoRepository;
    private final CardCompanyRepository cardCompanyRepository;

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
    @Transactional
    public Mono<CardInfoEntity> duplicationCardUseCheck(PaymentDto paymentDto) {
        return cardInfoRepository.findByCardNumber(cardComponent.getCardNumberHash(paymentDto.getCardNumber()))   //카드 정보 조회

                //카드 정보 미존재일 경우 신규 생성
                .switchIfEmpty(
                        Mono.just(CardInfoEntity.builder()
                                .key(null)  //신규 생성
                                .cardNumber(cardComponent.getCardNumberHash(paymentDto.getCardNumber()))
                                .cardInfo(cardComponent.getCardEncrypt(paymentDto))
                                .useStatus(false)
                                .build())
                )

                //사용 가능여부 확인
                .filter(cardInfoEntity -> !cardInfoEntity.getUseStatus())
                .switchIfEmpty(Mono.error(new CardUseException()));
    }

    /**
     * <pre>
     *  @methodName : cardPaymentSave
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-07 오후 1:03
     *  @summary : 카드사 통신 데이터 저장
     *  @param : [paymentDto, paymentActionType, paymentInstanceEntity]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.card.dao.CardCompanyEntity>
     * </pre>
     */
    @Transactional
    public Mono<CardCompanyEntity> cardPaymentSave(PaymentDto paymentDto, PaymentActionType paymentActionType, PaymentInstanceEntity paymentInstanceEntity) {
        return cardCompanyRepository.save(CardCompanyEntity.builder()              //카드 결제 정보 저장
                .key(null)
                .paymentInfo(cardComponent.getPaymentInfo(paymentDto, paymentActionType, paymentInstanceEntity.getCardInfo(), paymentInstanceEntity.getIdentityNumber()))
                .build());
    }

    /**
     * <pre>
     *  @methodName : useCardInfoEntity
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-07 오후 1:03
     *  @summary : 카드결제 진행
     *  @param : [cardInfoEntity]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.card.dao.CardInfoEntity>
     * </pre>
     */
    @Transactional
    public Mono<CardInfoEntity> useCardInfoEntity(CardInfoEntity cardInfoEntity) {
        cardInfoEntity.setUseStatus(true);                    //카드 결제 진행
        return cardInfoRepository.save(cardInfoEntity);
    }

    /**
     * <pre>
     *  @methodName : cardCancelSave
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-07 오후 1:03
     *  @summary : 카드 결제 취소
     *  @param : [paymentCancelDto, paymentInstanceEntity]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.card.dao.CardCompanyEntity>
     * </pre>
     */
    @Transactional
    public Mono<CardCompanyEntity> cardCancelSave(PaymentCancelDto paymentCancelDto, PaymentInstanceEntity paymentInstanceEntity) {
        return cardCompanyRepository.save(CardCompanyEntity.builder()
                .key(null)
                .paymentInfo(cardComponent.getCancelPaymentInfo(paymentCancelDto, paymentInstanceEntity, PaymentActionType.CANCEL))
                .build());
    }

    /**
     * <pre>
     *  @methodName : cardUseDone
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-07 오후 1:03
     *  @summary : 카드결제 완료
     *  @param : [cardInfoEntity]
     *  @return : reactor.core.publisher.Mono<com.github.renuevo.domain.card.dao.CardInfoEntity>
     * </pre>
     */
    @Transactional
    public Mono<CardInfoEntity> cardUseDone(CardInfoEntity cardInfoEntity) {
        cardInfoEntity.setUseStatus(false);
        return cardInfoRepository.save(cardInfoEntity);
    }

    /**
     * <pre>
     *  @methodName : getCardDecrypt
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-07 오후 1:04
     *  @summary : 카드정보 복호화
     *  @param : [cardEncrypt, salt]
     *  @return : com.github.renuevo.domain.card.dto.CardInfoDto
     * </pre>
     */
    public CardInfoDto getCardDecrypt(String cardEncrypt, String salt) {
        return cardComponent.getCardDecrypt(cardEncrypt, salt);
    }

}
