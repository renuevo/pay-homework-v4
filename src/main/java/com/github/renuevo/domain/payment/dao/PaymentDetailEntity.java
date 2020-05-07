package com.github.renuevo.domain.payment.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * <pre>
 * @className : PaymentDetailEntity
 * @author : Deokhwa.Kim
 * @since : 2020-05-03
 * @summary : 카드내역 Entity
 * </pre>
 */
@Getter
@NoArgsConstructor
@Table("payment_detail")
public class PaymentDetailEntity {

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


    @Builder
    public PaymentDetailEntity(Long key, String identityNumber, String paymentType, int installment, int price, int tax) {
        this.key = key;
        this.identityNumber = identityNumber;
        this.paymentType = paymentType;
        this.installment = installment;
        this.price = price;
        this.tax = tax;
        this.createDt = LocalDateTime.now();
    }

}
