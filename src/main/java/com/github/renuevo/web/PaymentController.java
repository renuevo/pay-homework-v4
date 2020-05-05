package com.github.renuevo.web;

import com.github.renuevo.service.PaymentService;
import com.github.renuevo.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * <pre>
 * @className : PaymentController
 * @author : Deokhwa.Kim
 * @since : 2020-05-01
 * </pre>
 */
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * <pre>
     *  @methodName : test
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-01 오후 11:02
     *  @summary : 결제
     *  @param : [cardPayDto, errors]
     *  @return : java.lang.String
     * </pre>
     */
    @PostMapping("/payment/save")
    public Mono<PaymentResponseDto> save(@ModelAttribute @Valid PaymentDto paymentDto) throws Exception {
        return paymentService.paymentSave(paymentDto);
    }

    /**
     * <pre>
     *  @methodName : cancel
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-01 오후 11:02
     *  @summary : 결제 취소
     *  @param : [id]
     *  @return : java.lang.String
     * </pre>
     */
    @PostMapping("/payment/cancel/{identityNumber}")
    public Mono<PaymentCancelResponseDto> cancel(@PathVariable String identityNumber, @ModelAttribute @Valid PaymentCancelDto paymentCancelDto) {
        paymentCancelDto.setIdentityNumber(identityNumber);
        return paymentService.paymentCancel(paymentCancelDto);
    }

    /**
     * <pre>
     *  @methodName : getPayment
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-01 오후 11:02
     *  @summary : 데이터 조회
     *  @param : [id]
     *  @return : java.lang.String
     * </pre>
     */
    @GetMapping("/payment/{identityNumber}")
    public Mono<PaymentViewResponseDto> viewPayment(@PathVariable String identityNumber) {
        return paymentService.getPaymentInfo(identityNumber);
    }

}

