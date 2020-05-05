package com.github.renuevo.exception.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentException extends Exception {
    private final Throwable throwable;
}
