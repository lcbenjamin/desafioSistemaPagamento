package com.lucascosta.desafiopagamento.core.ports.outbound;

import com.lucascosta.desafiopagamento.core.domain.payment.model.WalletHolder;

public interface WalletHolderRepositoryPort {

    WalletHolder findById(Long id);

    WalletHolder findByDocument(String document);

    WalletHolder findByEmail(String email);

    WalletHolder save(WalletHolder holder);

}
