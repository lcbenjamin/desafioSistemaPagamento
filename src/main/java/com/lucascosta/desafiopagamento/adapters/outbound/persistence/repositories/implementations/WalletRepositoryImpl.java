package com.lucascosta.desafiopagamento.adapters.outbound.persistence.repositories.implementations;

import com.lucascosta.desafiopagamento.util.mappers.WalletMapper;
import com.lucascosta.desafiopagamento.adapters.outbound.persistence.repositories.JpaWalletRepository;
import com.lucascosta.desafiopagamento.core.domain.payment.model.Wallet;
import com.lucascosta.desafiopagamento.core.ports.outbound.WalletRepositoryPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class WalletRepositoryImpl implements WalletRepositoryPort {

    private final JpaWalletRepository repository;
    private final WalletMapper mapper;

    public WalletRepositoryImpl(JpaWalletRepository repository, WalletMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Wallet save(Wallet wallet) {
        var entity = mapper.toEntity(wallet);
        var saved = repository.save(entity);
        return mapper.toModel(saved);
    }
}
