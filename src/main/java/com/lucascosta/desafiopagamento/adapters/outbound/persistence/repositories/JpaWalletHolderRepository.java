package com.lucascosta.desafiopagamento.adapters.outbound.persistence.repositories;

import com.lucascosta.desafiopagamento.adapters.outbound.persistence.entity.WalletHolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaWalletHolderRepository extends JpaRepository<WalletHolderEntity, Long> {

    Optional<WalletHolderEntity> findByDocument(String document);

    Optional<WalletHolderEntity> findByEmail(String email);
}
