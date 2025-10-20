package com.lucascosta.desafiopagamento.core.application;

import com.lucascosta.desafiopagamento.core.domain.payment.enums.TransferStatus;
import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.domain.payment.model.TransferResult;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferValidationChainFactory;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferValidationContext;
import com.lucascosta.desafiopagamento.core.ports.inbound.TransferUseCase;
import com.lucascosta.desafiopagamento.core.ports.outbound.persistence.WalletHolderRepositoryPort;
import com.lucascosta.desafiopagamento.core.ports.outbound.persistence.WalletRepositoryPort;
import com.lucascosta.desafiopagamento.core.ports.outbound.service.ClientAuthorizationServicePort;

import java.time.Instant;

public class TransferService implements TransferUseCase {

    private final WalletHolderRepositoryPort walletHolderRepository;
    private final WalletRepositoryPort walletRepository;
    private final ClientAuthorizationServicePort authorizationPort;

    public TransferService(
            WalletHolderRepositoryPort walletHolderRepository,
            WalletRepositoryPort walletRepository,
            ClientAuthorizationServicePort authorizationPort) {
        this.walletHolderRepository = walletHolderRepository;
        this.walletRepository = walletRepository;
        this.authorizationPort = authorizationPort;
    }

    @Override
    public TransferResult execute(Transfer transfer) {
        validateTransfer(transfer);
        authorizeTransferOrThrowError(transfer);
        return getSuccess(transfer);
    }

    private void validateTransfer(Transfer transfer) {
        TransferValidationChainFactory
                .create(walletHolderRepository, walletRepository)
                .handle(new TransferValidationContext(transfer));
    }

    private void authorizeTransferOrThrowError(Transfer transfer) {
        authorizationPort.authorizeTransferOrThrowError(transfer);
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
}
