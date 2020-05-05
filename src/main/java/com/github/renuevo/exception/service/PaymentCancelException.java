package com.github.renuevo.exception.service;

import com.github.renuevo.exception.ErrorResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class PaymentCancelException extends Exception{

    private Throwable throwable;
    private List<ErrorResponse.FieldError> fieldError;

    public PaymentCancelException(List<ErrorResponse.FieldError> fieldError){
        this.fieldError = fieldError;
    }

    public PaymentCancelException(Throwable throwable){
        this.throwable = throwable;
    }

}
