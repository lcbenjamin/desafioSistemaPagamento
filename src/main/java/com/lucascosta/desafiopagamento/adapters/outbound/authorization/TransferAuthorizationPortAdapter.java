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
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
@Slf4j
public class TransferAuthorizationPortAdapter implements TransferAuthorizationPort {

    private final AuthorizationProperties props;
    private final AuthorizationResponseMapper mapper;
    private final RestClient restClient;

    @Override
    public AuthorizationResult authorize(Transfer transfer) {
        AuthorizationApiResponse response = restClient
                .get()
                .uri(props.path())
                .retrieve()
                .body(AuthorizationApiResponse.class);

        AuthorizationResult result = mapper.toDomain(response);
        log.debug("Autorização externa respondida: status={}, authorization={}", result.status(), result.authorization());
        return result;
    }
}