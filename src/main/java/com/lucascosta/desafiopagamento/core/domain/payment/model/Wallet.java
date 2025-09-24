package com.lucascosta.desafiopagamento.core.domain.payment.model;

import java.math.BigDecimal;

public record Wallet(
        Long id,
        Long version,
        BigDecimal balance
) {
}
