package com.github.renuevo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig {

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
    public ModelMapper modelMapperConfig(){
        return new ModelMapper();
    }

}
