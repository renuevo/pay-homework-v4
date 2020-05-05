package com.github.renuevo.domain.payment;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PaymentDetailRepository extends ReactiveCrudRepository<PaymentDetailEntity, Long> {
}