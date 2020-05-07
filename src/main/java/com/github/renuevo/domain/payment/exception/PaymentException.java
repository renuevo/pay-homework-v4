package com.github.renuevo.domain.payment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <pre>
 * @className : PaymentException
 * @author : Deokhwa.Kim
 * @since : 2020-05-05
 * @summary : 결제 에러
 * </pre>
 */
@Getter
@AllArgsConstructor
public class PaymentException extends RuntimeException {
    private final Throwable throwable;
}
