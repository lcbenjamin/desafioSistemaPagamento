package com.lucascosta.desafiopagamento.infrastructure.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties(AuthorizationProperties.class)
public class AuthorizationClientConfig {

    @Bean
    public WebClient authorizationWebClient(AuthorizationProperties props) {

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(props.responseTimeout()))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, props.connectTimeout())
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(props.readTimeout()))
                                .addHandlerLast(new WriteTimeoutHandler(props.writeTimeout()))
                );

        return WebClient.builder()
                .baseUrl(props.baseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
