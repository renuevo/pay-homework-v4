package com.github.renuevo.domain.payment.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PaymentDetailRepository extends ReactiveCrudRepository<PaymentDetailEntity, Long> {
}