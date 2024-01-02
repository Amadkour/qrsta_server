package com.softkour.qrsta_server.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.net.UnknownHostException;

@Configuration
@EnableJpaRepositories({ "com.softkour.qrsta_server.repo" })
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration {
    // @Bean
    // public Docket api() {
    //
    // return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
    // .securityContexts(Arrays.asList(securityContext())).securitySchemes(Arrays.asList(apiKey())).select()
    // .apis(RequestHandlerSelectors.basePackage("com.abhicodes.springfoxswagger3.controller"))
    // .paths(PathSelectors.ant("/api/**")).build()
    // .globalRequestParameters(Arrays.asList(
    // new RequestParameterBuilder().name("x-global-header-1").description("Remote
    // User")
    // .in(ParameterType.HEADER).required(true)
    // .query(simpleParameterSpecificationBuilder ->
    // simpleParameterSpecificationBuilder
    // .allowEmptyValue(false)
    // .model(modelSpecificationBuilder -> modelSpecificationBuilder
    // .scalarModel(ScalarType.STRING)))
    // .build(),
    // new
    // RequestParameterBuilder().name("x-global-header-2").description("Impersonate
    // User")
    // .in(ParameterType.HEADER).required(false)
    // .query(simpleParameterSpecificationBuilder ->
    // simpleParameterSpecificationBuilder
    // .allowEmptyValue(false)
    // .model(modelSpecificationBuilder -> modelSpecificationBuilder
    // .scalarModel(ScalarType.STRING)))
    // .build()));
    // }
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info().title("My REST API")
                        .description("Some custom description of API.")
                        .version("1.0").contact(new Contact().name("Sallo Szrajbman")
                                .email("www.baeldung.com").url("salloszraj@gmail.com"))
                        .license(new License().name("License of API")
                                .url("API license URL")));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        return template;
    }
}