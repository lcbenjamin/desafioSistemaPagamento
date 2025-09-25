package com.lucascosta.desafiopagamento.infrastructure.config;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RetryConfig {

    @Bean
    public Retry authorizationApiRetry(RetryRegistry registry) {
        return registry.retry("authorizationApi");
    }
}

