package com.lucascosta.desafiopagamento.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
@EnableConfigurationProperties(AuthorizationProperties.class)
public class AuthorizationClientConfig {

    @Bean
    public RestClient authorizationRestClient(AuthorizationProperties props) {
        HttpClient jdkHttpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(props.connectTimeout()))
                .build();

        return RestClient.builder()
                .baseUrl(props.baseUrl())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .requestFactory(new JdkClientHttpRequestFactory(jdkHttpClient))
                .build();
    }

}
