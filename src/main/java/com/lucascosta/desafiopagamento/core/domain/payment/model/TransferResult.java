package com.lucascosta.desafiopagamento.core.domain.payment.model;

import com.lucascosta.desafiopagamento.core.domain.payment.enums.TransferStatus;

import java.time.Instant;

public record TransferResult(
        Long payerId,
        Long payeeId,
        TransferStatus status,
        Instant occurredAt,
        String failureReason
) {
}
