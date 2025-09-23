package com.lucascosta.desafiopagamento.core.ports.outbound;

import com.lucascosta.desafiopagamento.core.domain.payment.model.AuthorizationResult;

public interface TransferAuthorizationPort {

    AuthorizationResult authorize();
}
