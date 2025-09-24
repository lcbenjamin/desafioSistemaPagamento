package com.lucascosta.desafiopagamento.adapters.outbound.persistence.repositories;

import com.lucascosta.desafiopagamento.adapters.outbound.persistence.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTransferRepository extends JpaRepository<TransferEntity, Long> {
}
