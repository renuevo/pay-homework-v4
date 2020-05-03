package com.github.renuevo.domain.card;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CardDataRepository extends ReactiveCrudRepository<CardDataEntity, Long> {
}
