package com.github.renuevo.domain.payment;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table("payment")
@NoArgsConstructor
public class PaymentEntity {

    @Id
    private Long key;
    private Integer installment;
    private Integer price;
    private Integer tax;

    @Column("payment_info")
    private String paymentInfo;

    @Builder
    public PaymentEntity(Long key, int installment, Integer price, Integer tax, String paymentInfo) {
        this.key = key;
        this.installment = installment;
        this.price = price;
        this.tax = tax;
        this.paymentInfo = paymentInfo;
    }

}
