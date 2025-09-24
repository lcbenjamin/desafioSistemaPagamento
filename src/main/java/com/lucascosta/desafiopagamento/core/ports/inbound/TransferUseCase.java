package com.lucascosta.desafiopagamento.core.ports.inbound;

import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.domain.payment.model.TransferResult;

public interface TransferUseCase {

    TransferResult execute(Transfer transfer);
}
