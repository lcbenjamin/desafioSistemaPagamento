package com.lucascosta.desafiopagamento.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "external.authorization.api")
public record AuthorizationProperties(
        String baseUrl,
        String path,
        Duration connectTimeout,
        Duration responseTimeout,
        Duration readTimeout,
        Duration writeTimeout
) {
}
