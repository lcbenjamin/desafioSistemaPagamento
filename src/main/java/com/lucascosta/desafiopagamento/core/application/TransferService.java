package com.lucascosta.desafiopagamento.core.application;

import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferValidationChainFactory;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferValidationContext;
import com.lucascosta.desafiopagamento.core.ports.inbound.TransferResult;
import com.lucascosta.desafiopagamento.core.ports.inbound.TransferUseCase;
import com.lucascosta.desafiopagamento.core.ports.outbound.WalletHolderRepositoryPort;
import com.lucascosta.desafiopagamento.core.ports.outbound.WalletRepositoryPort;
import org.springframework.stereotype.Service;

@Service // Unica referencia ao Spring no core, para ser injetado em outros lugares
public class TransferService implements TransferUseCase {

    private final WalletHolderRepositoryPort walletHolderRepository;
    private final WalletRepositoryPort walletRepository;

    public TransferService(WalletHolderRepositoryPort walletHolderRepository, WalletRepositoryPort walletRepository) {
        this.walletHolderRepository = walletHolderRepository;
        this.walletRepository = walletRepository;
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
        //TODO: Implementar
    }
}
