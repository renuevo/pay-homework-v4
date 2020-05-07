package com.github.renuevo.domain.card.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * <pre>
 * @className : CardInfoEntity
 * @author : Deokhwa.Kim
 * @since : 2020-05-04
 * @summary : 카드 Entity
 * </pre>
 */
@Getter
@NoArgsConstructor
@Table("card_info")
public class CardInfoEntity {
    @Id
    private Long key;

    @Column("card_info")
    private String cardInfo;        // 암호화 카드정보

    @Column("card_number")
    private String cardNumber;        //해싱 카드번호

    @Setter
    @Column("use_status")
    private Boolean useStatus;      //사용 진행 여부 -> 이중 결제 방비


    @Builder
    public CardInfoEntity(Long key, String cardInfo, String cardNumber, Boolean useStatus) {
        this.key = key;
        this.cardInfo = cardInfo;
        this.cardNumber = cardNumber;
        this.useStatus = useStatus;
    }

}
