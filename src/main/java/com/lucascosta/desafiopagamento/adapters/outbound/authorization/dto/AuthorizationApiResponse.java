package com.lucascosta.desafiopagamento.adapters.outbound.authorization.dto;

public record AuthorizationApiResponse(
        String status,
        AuthorizationDataApiResponse data
) {
}
