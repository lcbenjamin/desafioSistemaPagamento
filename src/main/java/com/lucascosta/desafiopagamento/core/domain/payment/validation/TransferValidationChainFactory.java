package com.lucascosta.desafiopagamento.core.domain.payment.validation;

import com.lucascosta.desafiopagamento.core.domain.payment.validation.rules.*;
import com.lucascosta.desafiopagamento.core.ports.outbound.WalletHolderRepositoryPort;
import com.lucascosta.desafiopagamento.core.ports.outbound.WalletRepositoryPort;

public final class TransferValidationChainFactory {
    private TransferValidationChainFactory() {
        // Utility class
    }

    public static TransferHandler create(WalletHolderRepositoryPort walletHolderRepository, WalletRepositoryPort walletRepository) {
        return new DistinctParticipantsHandler()
                .linkWith(new LoadPayerHandler(walletHolderRepository))
                .linkWith(new LoadPayeeHandler(walletHolderRepository))
                .linkWith(new PayerTypeValidationHandler())
                .linkWith(new SufficientBalanceHandler(walletRepository));
    }
}