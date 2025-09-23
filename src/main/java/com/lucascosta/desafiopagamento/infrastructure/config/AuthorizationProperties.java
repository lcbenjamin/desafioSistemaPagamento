package com.lucascosta.desafiopagamento.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.authorization.api")
public record AuthorizationProperties(
        String baseUrl,
        String path,
        int connectTimeout,
        int readTimeout
) {
}
