package com.lucascosta.desafiopagamento.core.application;

import com.lucascosta.desafiopagamento.core.domain.exceptions.ExternalTransferUnauthorizedException;
import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.domain.payment.model.TransferResult;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferValidationChainFactory;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferValidationContext;
import com.lucascosta.desafiopagamento.core.ports.inbound.TransferUseCase;
import com.lucascosta.desafiopagamento.core.ports.outbound.TransferAuthorizationPort;
import com.lucascosta.desafiopagamento.core.ports.outbound.WalletHolderRepositoryPort;
import com.lucascosta.desafiopagamento.core.ports.outbound.WalletRepositoryPort;
import org.springframework.stereotype.Service;

@Service // Unica referencia ao Spring no core, para ser injetado em outros lugares
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
        autorizeTransfer(transfer);


        // TODO: debita o valor do pagador
        // TODO: credita o valor no recebedor
        // TODO: registra a transação
        // TODO: Notifica atraves de sistema externo o pagador que a transação foi realizada com sucesso
        return null;
    }

    private void validateTransfer(Transfer transfer) {
        TransferValidationChainFactory
                .create(walletHolderRepository, walletRepository)
                .handle(new TransferValidationContext(transfer));
    }

    private void autorizeTransfer(Transfer transfer) {
        var authorizationResult = authorizationPort.authorize();
        if (!authorizationResult.authorization()) {
            throw new ExternalTransferUnauthorizedException("Transferência não autorizada pelo sistema externo");
        }
    }
}
