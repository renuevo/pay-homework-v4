package com.github.renuevo.domain.card;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * <pre>
 * @className : CardDataEntity
 * @author : Deokhwa.Kim
 * @since : 2020-05-03
 * @summary : 카드사 통신 Entity
 * </pre>
 */
@NoArgsConstructor
@Table("card_data")
public class CardDataEntity {
    @Id
    private Long key;

    @Column("payment_info")
    private String paymentInfo;

    @Builder
    public CardDataEntity(Long key, String paymentInfo) {
        this.key = key;
        this.paymentInfo = paymentInfo;
    }

}
