package com.github.renuevo.domain.payment.common;

/**
 * <pre>
 * @className : ValidationConstant
 * @author : Deokhwa.Kim
 * @since : 2020-05-07
 * @summary : Validation 상수
 * </pre>
 */
public class ValidationConstant {
    private ValidationConstant(){}

    public final static int IDENTITY_NUMBER_LENGTH = 20;   //관리번호

    public final static int MIN_PAYMENT_TAX_AMOUNT = 0;         //최소 부가가치세 금액
    public final static int MAX_PAYMENT_TAX_AMOUNT = 10000000;  //최대 부가가치세 금액
    public final static int MIN_PAYMENT_PRICE_AMOUNT = 100;         //최소 결제 금액
    public final static int MAX_PAYMENT_PRICE_AMOUNT = 1000000000;  //최대 결제 금액
    public final static int MIN_INSTALLMENT_AMOUNT = 0;             //최소 할부
    public final static int MAX_INSTALLMENT_AMOUNT = 12;            //최대 할부

    public final static int CARD_VALIDITY_RANGE_LENGTH = 4;         //카드유효기간
    public final static int MIN_CARD_NUMBER_LENGTH = 10;            //카드번호 min
    public final static int MAX_CARD_NUMBER_LENGTH = 16;            //카드번호 max
    public final static int MIN_CVC = 100;                          //카드 cvc 최소값
    public final static int MAX_CVC = 999;                          //카드 cvc 최대값

}
