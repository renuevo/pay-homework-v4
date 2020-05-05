package com.github.renuevo.domain.payment;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public interface PaymentInstanceRepository extends ReactiveCrudRepository<PaymentInstanceEntity, Long> {
    @Query("SELECT * FROM payment_instance WHERE identity_number = :identityNumber")
    Mono<PaymentInstanceEntity> findByIdentityNumber(String identityNumber);
}
