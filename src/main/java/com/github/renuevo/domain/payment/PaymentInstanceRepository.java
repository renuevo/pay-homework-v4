package com.github.renuevo.domain.payment;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface PaymentInstanceRepository extends ReactiveCrudRepository<PaymentInstanceEntity, Long> {
}
