package com.github.renuevo.exception;

import com.github.renuevo.exception.service.CardUseException;
import com.github.renuevo.exception.service.PaymentCancelException;
import com.github.renuevo.exception.service.PaymentException;
import com.github.renuevo.exception.service.PaymentViewException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * <pre>
 * @className : GlobalExceptionHandler
 * @author : Deokhwa.Kim
 * @since : 2020-05-05
 * </pre>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * <pre>
     *  @methodName : cardUseException
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 2:22
     *  @summary :
     *  @param : [e]
     *  @return : com.github.renuevo.exception.ErrorResponse
     * </pre>
     */
    @ExceptionHandler(CardUseException.class)
    protected ResponseEntity<ErrorResponse> cardUseException(CardUseException e) {
        log.error("카드 이중 결제 Error");
        return ResponseEntity.badRequest().body(ErrorResponse.of(ErrorCode.CARD_USE_STATUS_ERROR));
    }

    /**
     * <pre>
     *  @methodName : paymentException
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 2:22
     *  @summary :
     *  @param : [e]
     *  @return : com.github.renuevo.exception.ErrorResponse
     * </pre>
     */
    @ExceptionHandler(PaymentException.class)
    protected ResponseEntity<ErrorResponse> paymentException(PaymentException e) {
        log.error("결제 Error : {}", e.getThrowable().getMessage(), e.getThrowable());
        return ResponseEntity.badRequest().body(ErrorResponse.of(ErrorCode.PAYMENT_ERROR));
    }

    /**
     * <pre>
     *  @methodName : paymentCancelException
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 2:22
     *  @summary :
     *  @param : [e]
     *  @return : com.github.renuevo.exception.ErrorResponse
     * </pre>
     */
    @ExceptionHandler(PaymentCancelException.class)
    protected ResponseEntity<ErrorResponse> paymentCancelException(PaymentCancelException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.PAYMENT_CANCEL_ERROR);
        if (e.getFieldError() != null) {
            log.error("결제 취소 Error : {}", e.getFieldError());
            errorResponse = ErrorResponse.of(ErrorCode.PAYMENT_CANCEL_ERROR, e.getFieldError());
        } else {
            log.error("결제 취소 Error : {}", e.getThrowable().getMessage(), e.getThrowable());
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * <pre>
     *  @methodName : paymentCancelException
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 2:22
     *  @summary :
     *  @param : [e]
     *  @return : com.github.renuevo.exception.ErrorResponse
     * </pre>
     */
    @ExceptionHandler(PaymentViewException.class)
    protected ResponseEntity<ErrorResponse> paymentViewException(PaymentViewException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.PAYMENT_VIEW_ERROR);
        if (e.getFieldError() != null) {
            log.error("결제 조회 Error : {}", e.getFieldError());
            errorResponse = ErrorResponse.of(ErrorCode.PAYMENT_VIEW_ERROR, e.getFieldError());
        } else {
            log.error("결제 조회 Error : {}", e.getThrowable().getMessage(), e.getThrowable());
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }




    /**
     * <pre>
     *  @methodName : handleMethodArgumentNotValidException
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 2:20
     *  @summary : Validated Error
     *  @param : [e]
     *  @return : com.github.renuevo.exception.ErrorResponse
     * </pre>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Validation Bind Error : {} ", e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult()));
    }

    /**
     * <pre>
     *  @methodName : handleBindException
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 2:21
     *  @summary : Parameter Binding Error
     *  @param : [e]
     *  @return : com.github.renuevo.exception.ErrorResponse
     * </pre>
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("Parameter Bind Error : {} ", e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult()));
    }

    /**
     * <pre>
     *  @methodName : handleMethodArgumentTypeMismatchException
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 2:21
     *  @summary : Enum Binding Error
     *  @param : [e]
     *  @return : com.github.renuevo.exception.ErrorResponse
     * </pre>
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("Enum Bind Error : {} ", e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(e));
    }

    /**
     * <pre>
     *  @methodName : handleException
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 2:21
     *  @summary : Default Exception Error
     *  @param : [e]
     *  @return : com.github.renuevo.exception.ErrorResponse
     * </pre>
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Error : {}", e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }

}
