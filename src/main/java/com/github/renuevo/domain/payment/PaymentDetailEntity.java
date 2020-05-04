package com.github.renuevo.domain.payment;

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
    private String identityNumber;

    @Column("payment_type")
    private String paymentType;
    private Integer installment;
    private Integer price;
    private Integer tax;

    @Column("create_dt")
    private LocalDateTime createDt; //생성일

    @Builder
    public PaymentDetailEntity(Long key, String identityNumber, String paymentType, Integer installment, Integer price, Integer tax) {
        this.key = key;
        this.identityNumber = identityNumber;
        this.paymentType = paymentType;
        this.installment = installment;
        this.price = price;
        this.tax = tax;
        this.createDt = LocalDateTime.now();
    }

}
