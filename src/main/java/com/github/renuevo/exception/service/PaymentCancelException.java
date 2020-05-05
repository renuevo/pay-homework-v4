package com.github.renuevo.exception.service;

import com.github.renuevo.web.dto.ErrorResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class PaymentCancelException extends Exception{

    @Getter
    List<ErrorResponseDto.FieldError> fieldError;

    public PaymentCancelException(List<ErrorResponseDto.FieldError> fieldError){
        this.fieldError = fieldError;
    }
}
