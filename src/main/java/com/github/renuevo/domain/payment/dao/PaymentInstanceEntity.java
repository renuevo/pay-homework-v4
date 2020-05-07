package com.github.renuevo.domain.payment.dao;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * <pre>
 * @className : PaymentInstanceEntity
 * @author : Deokhwa.Kim
 * @since : 2020-05-03
 * @summary : 결제 Instance
 * </pre>
 */
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Table("payment_instance")
public class PaymentInstanceEntity {

    @Id
    private Long key;                       //key
    private int installment;            //할부기간

    @Setter
    private int price;                  //가격

    @Setter
    private int tax;                    //부가가치세
    private String identityNumber;          //관리번호
    private String cancelIdentityNumber;    //전체취소 관리 번호

    @Setter
    private Boolean cancel;                 //최소 여부

    @Setter
    @Column("card_info")
    private String cardInfo;                //암호코드
    private String salt;                    //암호 salt


    @Column("create_dt")
    private LocalDateTime createDt;         //생성일


    @Builder
    public PaymentInstanceEntity(Long key, int installment, int price, int tax, String cardInfo, String salt, String identityNumber, String cancelIdentityNumber) {
        this.key = key;
        this.installment = installment;
        this.price = price;
        this.tax = tax;
        this.cardInfo = cardInfo;
        this.salt = salt;
        this.identityNumber = identityNumber;
        this.cancelIdentityNumber = cancelIdentityNumber;
        this.createDt = LocalDateTime.now();
    }

}
