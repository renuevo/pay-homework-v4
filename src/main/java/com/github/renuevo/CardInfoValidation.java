package com.github.renuevo;

import com.github.renuevo.domain.card.CardInfoEntity;

import java.util.function.Predicate;

public class CardInfoValidation {
    public Predicate<CardInfoEntity> cardUseStatus = cardInfoEntity -> !cardInfoEntity.getUseStatus();
}
