package com.lucascosta.desafiopagamento.core.ports.outbound;

import com.lucascosta.desafiopagamento.core.domain.payment.model.Wallet;

public interface WalletRepositoryPort {

    Wallet save(Wallet wallet);

}
