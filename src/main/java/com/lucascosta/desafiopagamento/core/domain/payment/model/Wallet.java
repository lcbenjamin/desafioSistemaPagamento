package com.lucascosta.desafiopagamento.core.domain.payment.model;

import java.math.BigDecimal;

public record Wallet(
        Long version,
        BigDecimal balance
) {
}
