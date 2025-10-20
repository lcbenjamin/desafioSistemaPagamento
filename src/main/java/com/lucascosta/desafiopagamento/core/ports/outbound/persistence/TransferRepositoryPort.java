package com.lucascosta.desafiopagamento.core.ports.outbound.persistence;

import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;

import java.util.Optional;

public interface TransferRepositoryPort {

    Transfer save(Transfer transfer);

    Optional<Transfer> findById(Long id);
}
