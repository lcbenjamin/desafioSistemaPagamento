package com.lucascosta.desafiopagamento.core.ports.outbound.service;

import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;

public interface ClientAuthorizationServicePort {

    void authorizeTransferOrThrowError(Transfer transfer);
}
