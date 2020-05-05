package com.github.renuevo.domain.payment;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PaymentDetailRepository extends ReactiveCrudRepository<PaymentDetailEntity, Long> {

    @Query("SELECT payment_detail.*, pi.price AS result_price, pi.tax AS result_tax, pi.installment AS result_installment" +
            "FROM payment_detail INNER JOIN payment_instance pi ON payment_detail.identity_number = pi.identity_number " +
            "WHERE payment_detail.identity_number = :identityNumber ORDER BY payment_detail.create_dt desc limit 1")
    Mono<PaymentDetailEntity> findByIdentityNumberSearch(String identityNumber);

}
