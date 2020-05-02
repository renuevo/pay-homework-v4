package com.github.renuevo.domain.payment;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface PaymentRepository extends ReactiveCrudRepository<PaymentEntity, Integer> {
}
