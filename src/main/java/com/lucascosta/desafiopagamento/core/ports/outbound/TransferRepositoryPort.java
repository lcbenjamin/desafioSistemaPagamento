package com.lucascosta.desafiopagamento.core.ports.outbound;

import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;

public interface TransferRepositoryPort {

    Transfer save(Transfer transfer);

    Transfer findById(Long id);
}
