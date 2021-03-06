package com.github.renuevo.domain.payment.module;

import com.github.renuevo.domain.payment.dao.PaymentInstanceEntity;
import com.github.renuevo.grobal.error.ErrorResponse;
import com.github.renuevo.domain.payment.exception.PaymentCancelException;
import com.github.renuevo.domain.payment.dto.PaymentCancelDto;

/**
 * <pre>
 * @className : Validation
 * @author : Deokhwa.Kim
 * @since : 2020-05-05
 * </pre>
 */
public class PaymentValidation {

    /**
     * <pre>
     *  @methodName : paymentCancelValidation
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 9:20
     *  @summary : 결제 취소 validation check
     *  @param : [paymentInstanceEntity, paymentCancelDto]
     *  @return : boolean
     * </pre>
     */
    public static boolean paymentCancelValidation(PaymentInstanceEntity paymentInstanceEntity, PaymentCancelDto paymentCancelDto) {

        //결제 취소여부
        boolean priceZero = (paymentInstanceEntity.getPrice() - paymentCancelDto.getPrice()) == 0;

        //전체 취소 여부 확인
        if (paymentInstanceEntity.getCancel())
            throw new PaymentCancelException(ErrorResponse.FieldError.of("cancel", "", "이미 취소된 결제 입니다"));

        //결제 금액보다 취소금액이 많음
        if (paymentInstanceEntity.getPrice() < paymentCancelDto.getPrice())
            throw new PaymentCancelException(ErrorResponse.FieldError.of("price", String.valueOf(paymentInstanceEntity.getPrice()), String.format("취소금액을 확인해 주세요.  결제 금액 : %s / 취소금액 : %s", paymentInstanceEntity.getPrice(), paymentCancelDto.getPrice())));

        //부가가치세가 취소금액이 많음
        if (paymentInstanceEntity.getTax() < paymentCancelDto.getTax()) {

            if (!paymentCancelDto.taxCheck())    //자동계산 X
                throw new PaymentCancelException(ErrorResponse.FieldError.of("tax", String.valueOf(paymentInstanceEntity.getTax()), String.format("취소금액을 확인해 주세요.  결제 금액 : %s / 취소금액 : %s", paymentInstanceEntity.getTax(), paymentCancelDto.getTax())));

            if (paymentCancelDto.taxCheck() && !priceZero)  //자동계산 결제 취소 X
                throw new PaymentCancelException(ErrorResponse.FieldError.of("tax", String.valueOf(paymentInstanceEntity.getTax()), String.format("취소금액을 확인해 주세요.  결제 금액 : %s / 취소금액 : %s", paymentInstanceEntity.getTax(), paymentCancelDto.getTax())));
        }

        //결제금액보다 부가가치세가 많음
        if (paymentCancelDto.getPrice() < paymentCancelDto.getTax())
            throw new PaymentCancelException(ErrorResponse.FieldError.of("tax", String.valueOf(paymentInstanceEntity.getTax()), String.format("%s 은 %s보다 작아야 합니다", paymentCancelDto.getTax(), paymentCancelDto.getPrice())));

        //결제는 만족 했지만 부가가치세가 남음
        if (priceZero && !paymentCancelDto.taxCheck() && paymentInstanceEntity.getTax() > paymentCancelDto.getTax())
            throw new PaymentCancelException(ErrorResponse.FieldError.of("tax", String.valueOf(paymentCancelDto.getTax()), String.format("취소되지 않은 부가가치세가 남습니다.  결제 금액 : %s / 취소금액 : %s", paymentInstanceEntity.getTax(), paymentCancelDto.getTax())));

        return true;
    }
}
