package com.lucascosta.desafiopagamento.adapters.outbound.persistence.repositories.implementations;

import com.lucascosta.desafiopagamento.adapters.outbound.persistence.mappers.TransferEntityMapper;
import com.lucascosta.desafiopagamento.adapters.outbound.persistence.repositories.JpaTransferRepository;
import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.ports.outbound.TransferRepositoryPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class TransferRepositoryImpl implements TransferRepositoryPort {

    private final JpaTransferRepository repository;
    private final TransferEntityMapper mapper;

    public TransferRepositoryImpl(JpaTransferRepository repository, TransferEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public Transfer save(Transfer transfer) {
        var entity = mapper.toEntity(transfer);
        var saved = repository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public Transfer findById(Long id) {
        return repository.findById(id).map(mapper::toModel).orElse(null);
    }
}
