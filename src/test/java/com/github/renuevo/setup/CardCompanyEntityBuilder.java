package com.github.renuevo.setup;


import com.github.renuevo.domain.card.dao.CardCompanyEntity;

public class CardCompanyEntityBuilder {

    public static CardCompanyEntity build() {
        return CardCompanyEntity.builder()
                .key(1L)
                .paymentInfo(" 446CANCEL    d1fff0b64a34dd60cda21234567890123456    001125777       10000000000006a8b725c987a6f93fc36755dd8e0d48749d4e99ddc576a724fc852bbbed51c529482efbda4caa230cf283832f174dda96fbd0385489b226c6782                                                                                                                                                                                                                                                           ")
                .build();
    }

    public static CardCompanyEntity newBuild() {
        return CardCompanyEntity.builder()
                .key(null)
                .paymentInfo(" 446CANCEL    d1fff0b64a34dd60cda21234567890123456    001125777       10000000000006a8b725c987a6f93fc36755dd8e0d48749d4e99ddc576a724fc852bbbed51c529482efbda4caa230cf283832f174dda96fbd0385489b226c6782                                                                                                                                                                                                                                                           ")
                .build();
    }

}
