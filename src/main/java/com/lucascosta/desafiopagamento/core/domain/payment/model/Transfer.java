package com.lucascosta.desafiopagamento.core.domain.payment.model;

import com.lucascosta.desafiopagamento.core.domain.payment.enums.TransferStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record Transfer(
        Long payerId,
        Long payeeId,
        BigDecimal amount,
        TransferStatus status,
        Instant createdAt,
        Instant finishedAt,
        String failureReason
        ) {
}
