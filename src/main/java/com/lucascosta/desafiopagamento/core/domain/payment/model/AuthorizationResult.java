package com.lucascosta.desafiopagamento.core.domain.payment.model;

public record AuthorizationResult(
        String status,
        boolean authorization
) {
}

