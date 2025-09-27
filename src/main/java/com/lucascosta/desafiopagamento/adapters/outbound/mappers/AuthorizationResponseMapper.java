package com.lucascosta.desafiopagamento.adapters.outbound.mappers;


import com.lucascosta.desafiopagamento.adapters.outbound.authorization.AuthorizationApiResponse;
import com.lucascosta.desafiopagamento.core.domain.payment.model.AuthorizationResult;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthorizationResponseMapper {

    public AuthorizationResult toDomain(AuthorizationApiResponse response) {
        if (response == null) {
            throw new IllegalStateException("Resposta nula do autorizador externo");
        }
        var data = Objects.requireNonNull(response.data(), "Campo 'data' ausente na resposta");
        return new AuthorizationResult(response.status(), data.authorization());
    }
}