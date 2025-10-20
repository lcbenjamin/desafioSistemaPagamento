package com.lucascosta.desafiopagamento.adapters.outbound.authorization;

import com.lucascosta.desafiopagamento.adapters.outbound.authorization.dto.AuthorizationApiResponse;
import com.lucascosta.desafiopagamento.adapters.outbound.mappers.AuthorizationResponseMapper;
import com.lucascosta.desafiopagamento.core.domain.payment.model.AuthorizationResult;
import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.ports.outbound.http.TransferAuthorizationPort;
import com.lucascosta.desafiopagamento.infrastructure.config.AuthorizationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransferAuthorizationPortAdapter implements TransferAuthorizationPort {

    private final AuthorizationProperties props;
    private final AuthorizationResponseMapper mapper;
    private final WebClient webClient;

    @Override
    public AuthorizationResult authorize(Transfer transfer) {
        var authorizationResult = webClient.get()
                .uri(props.path())
                .retrieve()
                .bodyToMono(AuthorizationApiResponse.class)
                .map(mapper::toDomain)
                .block();
        log.info("Resultado da autorização recebida: {}", authorizationResult);
        return authorizationResult;
    }

}