package com.github.renuevo.domain.payment.exception;

import com.github.renuevo.grobal.error.ErrorResponse;
import lombok.Getter;

import java.util.List;

/**
 * <pre>
 * @className : PaymentViewException
 * @author : Deokhwa.Kim
 * @since : 2020-05-05
 * @summary : 조회 에러
 * </pre>
 */
@Getter
public class PaymentViewException extends RuntimeException {
    private Throwable throwable;
    private List<ErrorResponse.FieldError> fieldError;

    public PaymentViewException(List<ErrorResponse.FieldError> fieldError) {
        this.fieldError = fieldError;
    }

    public PaymentViewException(Throwable throwable) {
        this.throwable = throwable;
    }
}
