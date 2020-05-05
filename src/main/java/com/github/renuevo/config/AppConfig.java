package com.github.renuevo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class AppConfig {

    /**
     * <pre>
     *  @methodName : modelMapperConfig
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오전 11:58
     *  @summary : 모델 바인드 빈
     *  @param : []
     *  @return : org.modelmapper.ModelMapper
     * </pre>
     */
    @Bean
    public ModelMapper modelMapperConfig() {
        return new ModelMapper();
    }


    /**
     * <pre>
     *  @methodName : webFluxConfigurer
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 9:35
     *  @summary : webflux ObjectMapper Setting
     *  @param : [objectMapper]
     *  @return : org.springframework.web.reactive.config.WebFluxConfigurer
     * </pre>
     */
    @Bean
    WebFluxConfigurer webFluxConfigurer(ObjectMapper objectMapper) {
        return new WebFluxConfigurer() {
            @Override
            public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
                configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
                configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
            }
        };
    }

}
