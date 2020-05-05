package com.github.renuevo.exception;

import com.github.renuevo.exception.service.CardUseException;
import com.github.renuevo.exception.service.PaymentCancelException;
import com.github.renuevo.exception.service.PaymentException;
import com.github.renuevo.web.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CardUseException.class)
    protected ErrorResponseDto cardUseException(CardUseException e) {
        log.error("카드 이중 결제 Error : {}", e.getMessage(), e);
        return ErrorResponseDto.of(ErrorCode.CARD_USE_STATUS_ERROR);
    }

    @ExceptionHandler(PaymentException.class)
    protected ErrorResponseDto paymentException(PaymentException e) {
        log.error("결제 Error : {}", e.getMessage(), e);
        return ErrorResponseDto.of(ErrorCode.PAYMENT_ERROR);
    }


    @ExceptionHandler(PaymentCancelException.class)
    protected ErrorResponseDto paymentCancelException(PaymentCancelException e) {
        log.error("결제 취소 Error : {}", e.getMessage(), e);

        if (e.getFieldError() != null)
            return ErrorResponseDto.of(ErrorCode.PAYMENT_CANCEL_ERROR, e.getFieldError());
        else
            return ErrorResponseDto.of(ErrorCode.PAYMENT_CANCEL_ERROR);
    }


    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ErrorResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Validation Bind Error : {} ", e.getMessage(), e);
        return ErrorResponseDto.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
    }

    /**
     * @ModelAttribut 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
    protected ErrorResponseDto handleBindException(BindException e) {
        log.error("Parameter Bind Error : {} ", e.getMessage(), e);
        return ErrorResponseDto.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ErrorResponseDto handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("Enum Bind Error : {} ", e.getMessage(), e);
        return ErrorResponseDto.of(e);
    }


    //그외 모든 에러 처리
    @ExceptionHandler(Exception.class)
    protected ErrorResponseDto handleException(Exception e) {
        log.error("Error : {}", e.getMessage(), e);
        return ErrorResponseDto.of(ErrorCode.INTERNAL_SERVER_ERROR);
    }

}
