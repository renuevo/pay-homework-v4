package com.github.renuevo.domain.card;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CardInfoRepository extends ReactiveCrudRepository<CardInfoEntity, Long> {
    @Query("SELECT * FROM card_info WHERE card_number = :cardNumber")
    Mono<CardInfoEntity> findByCardNumber(String cardNumber);
}
