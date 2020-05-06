package com.github.renuevo.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.data.r2dbc.connectionfactory.init.CompositeDatabasePopulator;
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer;
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator;
import org.springframework.transaction.ReactiveTransactionManager;

/**
 * <pre>
 * @className : R2dbcConfig
 * @author : Deokhwa.Kim
 * @since : 2020-05-02
 * </pre>
 */
@Configuration
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    @Value("${spring.r2dbc.url}")
    String r2dbcUrl;

    /**
     * <pre>
     *  @methodName : connectionFactory
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-02 오후 4:40
     *  @summary : connection factory 설정
     *  @param : []
     *  @return : io.r2dbc.spi.ConnectionFactory
     * </pre>
     */
    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(r2dbcUrl);
    }

    /**
     * <pre>
     *  @methodName : initializer
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-02 오후 4:41
     *  @summary : 스키마 초기화
     *  @param : [connectionFactory]
     *  @return : org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer
     * </pre>
     */
    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        initializer.setDatabasePopulator(populator);

        return initializer;
    }

    /**
     * <pre>
     *  @methodName : transactionManager
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-02 오후 4:41
     *  @summary : 트랜잭션 메니저 정의
     *  @param : [connectionFactory]
     *  @return : org.springframework.transaction.ReactiveTransactionManager
     * </pre>
     */
    @Bean
    @ConditionalOnMissingBean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

}
