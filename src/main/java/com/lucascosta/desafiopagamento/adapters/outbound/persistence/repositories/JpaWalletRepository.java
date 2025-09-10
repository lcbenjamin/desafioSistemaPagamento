package com.lucascosta.desafiopagamento.adapters.outbound.persistence.repositories;

import com.lucascosta.desafiopagamento.adapters.outbound.persistence.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaWalletRepository extends JpaRepository<WalletEntity, Long> {

}
