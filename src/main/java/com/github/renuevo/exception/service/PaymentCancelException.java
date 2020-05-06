package com.github.renuevo.exception.service;

import com.github.renuevo.exception.ErrorResponse;
import lombok.Getter;

import java.util.List;

/**
 * <pre>
 * @className : PaymentCancelException
 * @author : Deokhwa.Kim
 * @since : 2020-05-05
 * @summary : 결제 취소 에러
 * </pre>
 */
@Getter
public class PaymentCancelException extends RuntimeException {

    private Throwable throwable;
    private List<ErrorResponse.FieldError> fieldError;

    public PaymentCancelException(List<ErrorResponse.FieldError> fieldError) {
        this.fieldError = fieldError;
    }

    public PaymentCancelException(Throwable throwable) {
        this.throwable = throwable;
    }

}
