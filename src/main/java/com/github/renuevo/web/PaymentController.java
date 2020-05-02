package com.github.renuevo.web;

import com.github.renuevo.service.PaymentService;
import com.github.renuevo.web.dto.CardPayDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <pre>
 * @className : PaymentController
 * @author : Deokhwa.Kim
 * @since : 2020-05-01
 * </pre>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
//@PreAuthorize("permitAll()")
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
    public String test(@ModelAttribute @Valid CardPayDto cardPayDto, Errors errors) {
        return null;
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
    @PostMapping("/payment/cancel/{id}")
    public String cancel(@PathVariable String id) {
        return null;
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
    @GetMapping("/payment/{id}")
    public String getPayment(@PathVariable String id) {
        return null;
    }

}
