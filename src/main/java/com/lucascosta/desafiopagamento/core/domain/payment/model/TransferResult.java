package com.lucascosta.desafiopagamento.core.domain.payment.model;

import java.time.Instant;

public record TransferResult(
        Long payerId,
        Long payeeId,
        String status,
        Instant occurredAt,
        String failureReason
) {
}
