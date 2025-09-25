package com.lucascosta.desafiopagamento.adapters.outbound.persistence.repositories.implementations;

import com.lucascosta.desafiopagamento.adapters.outbound.persistence.mappers.WalletHolderMapper;
import com.lucascosta.desafiopagamento.adapters.outbound.persistence.repositories.JpaWalletHolderRepository;
import com.lucascosta.desafiopagamento.core.domain.payment.model.WalletHolder;
import com.lucascosta.desafiopagamento.core.ports.outbound.WalletHolderRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class WalletHolderRepositoryImpl implements WalletHolderRepositoryPort {

    private final JpaWalletHolderRepository repository;
    private final WalletHolderMapper mapper;

    public WalletHolderRepositoryImpl(JpaWalletHolderRepository repository, WalletHolderMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<WalletHolder> findById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<WalletHolder> findByDocument(String document) {
        return repository.findByDocument(document).map(mapper::toModel);
    }

    @Override
    public Optional<WalletHolder> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toModel);
    }

    @Override
    public WalletHolder save(WalletHolder holder) {
        return mapper.toModel(repository.save(mapper.toEntity(holder)));
    }
}
