package com.lucascosta.desafiopagamento.core.ports.inbound;

import java.time.Instant;

public record TransferResult(
        Long payerId,
        Long payeeId,
        String status,
        Instant occurredAt,
        String failureReason
) {
}
