package com.lucascosta.desafiopagamento.core.domain.payment.validation;

import com.lucascosta.desafiopagamento.core.domain.payment.validation.rules.*;
import com.lucascosta.desafiopagamento.core.ports.outbound.persistence.WalletHolderRepositoryPort;
import com.lucascosta.desafiopagamento.core.ports.outbound.persistence.WalletRepositoryPort;

public final class TransferValidationChainFactory {
    private TransferValidationChainFactory() {
        // Utility class
    }

    public static TransferHandler create(WalletHolderRepositoryPort walletHolderRepository, WalletRepositoryPort walletRepository) {
        return TransferValidationChainBuilder.builder()
                .add(new DistinctParticipantsHandler())
                .add(new LoadPayerHandler(walletHolderRepository))
                .add(new LoadPayeeHandler(walletHolderRepository))
                .add(new PayerTypeValidationHandler())
                .add(new SufficientBalanceHandler())
                .build();
    }
}