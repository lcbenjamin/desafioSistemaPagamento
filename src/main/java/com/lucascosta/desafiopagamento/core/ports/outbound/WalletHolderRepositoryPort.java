package com.lucascosta.desafiopagamento.core.ports.outbound;

import com.lucascosta.desafiopagamento.core.domain.payment.model.WalletHolder;

import java.util.Optional;

public interface WalletHolderRepositoryPort {

    Optional<WalletHolder> findById(Long id);

    Optional<WalletHolder> findByDocument(String document);

    Optional<WalletHolder> findByEmail(String email);

    WalletHolder save(WalletHolder holder);

}
