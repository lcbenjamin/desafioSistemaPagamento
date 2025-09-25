package com.lucascosta.desafiopagamento.core.application;

import com.lucascosta.desafiopagamento.core.domain.exceptions.ExternalTransferUnauthorizedException;
import com.lucascosta.desafiopagamento.core.domain.payment.enums.TransferStatus;
import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.domain.payment.model.TransferResult;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferValidationChainFactory;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferValidationContext;
import com.lucascosta.desafiopagamento.core.ports.inbound.TransferUseCase;
import com.lucascosta.desafiopagamento.core.ports.outbound.TransferAuthorizationPort;
import com.lucascosta.desafiopagamento.core.ports.outbound.WalletHolderRepositoryPort;
import com.lucascosta.desafiopagamento.core.ports.outbound.WalletRepositoryPort;

import java.time.Instant;

public class TransferService implements TransferUseCase {

    private final WalletHolderRepositoryPort walletHolderRepository;
    private final WalletRepositoryPort walletRepository;
    private final TransferAuthorizationPort authorizationPort;

    public TransferService(WalletHolderRepositoryPort walletHolderRepository,
                           WalletRepositoryPort walletRepository,
                           TransferAuthorizationPort authorizationPort) {
        this.walletHolderRepository = walletHolderRepository;
        this.walletRepository = walletRepository;
        this.authorizationPort = authorizationPort;
    }

    @Override
    public TransferResult execute(Transfer transfer) {
        validateTransfer(transfer);
        authorizeTransfer(transfer);

        return getSuccess(transfer);
    }

    private static TransferResult getSuccess(Transfer transfer) {
        // Temporario, até implementar a persistência
        return new TransferResult(
                transfer.payerId(),
                transfer.payeeId(),
                TransferStatus.COMPLETED,
                Instant.now(),
                null
        );
    }

    private void validateTransfer(Transfer transfer) {
        TransferValidationChainFactory
                .create(walletHolderRepository, walletRepository)
                .handle(new TransferValidationContext(transfer));
    }

    private void authorizeTransfer(Transfer transfer) {
        var authorizationResult = authorizationPort.authorize(transfer);
        if (!authorizationResult.authorization()) {
            throw new ExternalTransferUnauthorizedException("Transferência não autorizada pelo sistema externo");
        }
    }
}
