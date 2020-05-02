package com.github.renuevo.domain.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor
@Table("payment_detail")
public class PaymentDetailEntity {

    @Column("payment_type")
    private String paymentType;
    private Integer installment;
    private Integer price;
    private int tax;

    @Builder
    public PaymentDetailEntity(String paymentType, Integer installment, Integer price) {
        this.paymentType = paymentType;
        this.installment = installment;
        this.price = price;
    }

}
