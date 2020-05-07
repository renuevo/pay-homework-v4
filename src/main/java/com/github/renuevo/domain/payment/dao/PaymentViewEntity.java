package com.github.renuevo.domain.payment.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * <pre>
 * @className : PaymentViewEntity
 * @author : Deokhwa.Kim
 * @since : 2020-05-05
 * </pre>
 */
@Getter
@Table("payment_detail")
public class PaymentViewEntity {

    @Id
    private Long key;

    @Column("identity_number")
    private String identityNumber;          //관리번호

    @Column("payment_type")
    private String paymentType;             //결제/취소
    private int installment;                //할부개월
    private int price;                      //금액
    private int tax;                        //부가가치세

    @Column("create_dt")
    private LocalDateTime createDt;         //생성일

    //결제 정보
    private int resultPrice;                //남은 금액
    private int resultTax;                  //남은 부가가치세
    private int resultInstallment;          //할부 개월
    private String cardInfo;                //암호코드
    private String salt;                    //암호 salt

}
