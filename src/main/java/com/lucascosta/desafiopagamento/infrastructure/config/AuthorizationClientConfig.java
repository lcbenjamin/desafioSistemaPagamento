package com.lucascosta.desafiopagamento.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(AuthorizationProperties.class)
public class AuthorizationClientConfig {

    @Bean
    RestClient authorizationRestClient(AuthorizationProperties props) {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(props.connectTimeout());
        factory.setReadTimeout(props.readTimeout());

        return RestClient.builder()
                .baseUrl(props.baseUrl())
                .requestFactory(factory)
                .build();
    }

}
