package com.github.renuevo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.util.Optional;

/**
 * <pre>
 * @className : SwaggerConfig
 * @author : Deokhwa.Kim
 * @since : 2020-05-05
 * </pre>
 */
@Configuration
@EnableSwagger2WebFlux
public class SwaggerConfig implements WebFluxConfigurer {
    /**
     * <pre>
     *  @methodName : api
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 9:54
     *  @summary : Wagger 기본설정
     *  @param : []
     *  @return : springfox.documentation.spring.web.plugins.Docket
     * </pre>
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.github.renuevo.web"))
                .build()
                .genericModelSubstitutes(Optional.class, Flux.class, Mono.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Pay Rest API")
                .build();
    }

    /**
     * <pre>
     *  @methodName : addResourceHandlers
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-05 오후 9:53
     *  @summary : Swagger Resource Path 설정
     *  @param : [registry]
     *  @return : void
     * </pre>
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/swagger-ui.html**")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
