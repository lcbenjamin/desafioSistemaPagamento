package com.lucascosta.desafiopagamento.adapters.outbound.authorization;

import com.lucascosta.desafiopagamento.core.domain.payment.model.AuthorizationResult;
import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.ports.outbound.TransferAuthorizationPort;
import com.lucascosta.desafiopagamento.infrastructure.config.AuthorizationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TransferAuthorizationPortAdapter implements TransferAuthorizationPort {

    private final RestClient authorizationRestClient;
    private final AuthorizationProperties props;

    @Override
    public AuthorizationResult authorize(Transfer transfer) {
        try {
            var apiResponse = authorizationRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(props.path())
                            .build())
                    .retrieve()
                    .body(AuthorizationApiResponse.class);

            return mapToDomain(apiResponse);
        } catch (RestClientResponseException ex) {
            throw new RuntimeException("Erro ao consultar autorizador externo: " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Falha ao consultar autorizador externo", ex);
        }
    }

    private AuthorizationResult mapToDomain(AuthorizationApiResponse response) {
        if (response == null) {
            throw new IllegalStateException("Resposta nula do autorizador externo");
        }
        var data = Objects.requireNonNull(response.data(), "Campo 'data' ausente na resposta");
        return new AuthorizationResult(response.status(), data.authorization());
    }
}