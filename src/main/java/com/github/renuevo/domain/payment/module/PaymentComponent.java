package com.github.renuevo.domain.payment.module;

import com.github.renuevo.grobal.module.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * @className : CardInfoUtils
 * @author : Deokhwa.Kim
 * @since : 2020-05-03
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class PaymentComponent {

    private final SecurityUtils securityUtils;

    /**
     * <pre>
     *  @methodName : getIdentityNumber
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-04 오후 10:16
     *  @summary : 관리번호 생성
     *  @param : []
     *  @return : java.lang.String
     * </pre>
     */
    public String getIdentityNumber() {
        return securityUtils.getIdentityNumber();
    }

    //암호화 salt 값
    public String getSalt() {
        return securityUtils.getSaltKey();
    }

}
