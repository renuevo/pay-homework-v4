package com.github.renuevo.exception.service;

import com.github.renuevo.exception.ErrorResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class PaymentViewException extends Exception {
    private Throwable throwable;
    private List<ErrorResponse.FieldError> fieldError;

    public PaymentViewException(List<ErrorResponse.FieldError> fieldError){
        this.fieldError = fieldError;
    }
    public PaymentViewException(Throwable throwable){
        this.throwable = throwable;
    }
}
