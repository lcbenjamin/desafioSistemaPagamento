package com.lucascosta.desafiopagamento.adapters.outbound.authorization;

public record AuthorizationApiResponse(
        String status,
        AuthorizationDataApiResponse data
) {
}
