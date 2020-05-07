package com.github.renuevo.setup;

import com.github.renuevo.domain.card.dao.CardInfoEntity;

public class CardInfoEntityBuilder {

    public static CardInfoEntity build() {
        return CardInfoEntity.builder()
                .key(1L)
                .cardNumber("######")
                .cardInfo("c75d2519eba6b3b99495547074bc7f6fb1541375261e0f2d68db714cbc1312bb18ce41313e18ab0611bf5679a7b4b279")
                .useStatus(false)
                .build();
    }

    public static CardInfoEntity usebuild() {
        return CardInfoEntity.builder()
                .key(1L)
                .cardNumber("######")
                .cardInfo("c75d2519eba6b3b99495547074bc7f6fb1541375261e0f2d68db714cbc1312bb18ce41313e18ab0611bf5679a7b4b279")
                .useStatus(true)
                .build();
    }

    public static CardInfoEntity newBuild() {
        return CardInfoEntity.builder()
                .key(null)
                .cardNumber("######")
                .cardInfo("c75d2519eba6b3b99495547074bc7f6fb1541375261e0f2d68db714cbc1312bb18ce41313e18ab0611bf5679a7b4b279")
                .useStatus(false)
                .build();
    }

}
