package com.github.renuevo.domain.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Table("payment_instance")
@NoArgsConstructor
public class PaymentInstanceEntity {

    @Id
    private Long key;               //관리번호
    private Integer installment;    //할부기간
    private Integer price;          //가격
    private Integer tax;            //부가가치세

    @Setter
    @Column("card_info")
    private String cardInfo; //암호코드
    private String salt;        //암호 salt


    @Column("create_dt")
    private LocalDateTime createDt; //생성일
    @Column("update_dt")
    private LocalDateTime updateDt; //수정일

    @Builder
    public PaymentInstanceEntity(Long key, Integer installment, Integer price, Integer tax, String cardInfo, String salt) {
        this.key = key;
        this.installment = installment;
        this.price = price;
        this.tax = tax;
        this.cardInfo = cardInfo;
        this.salt = salt;


        this.createDt = LocalDateTime.now();
        this.updateDt = LocalDateTime.now();

    }

}
