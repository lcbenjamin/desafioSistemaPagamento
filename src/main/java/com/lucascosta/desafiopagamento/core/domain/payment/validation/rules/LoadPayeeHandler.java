package com.lucascosta.desafiopagamento.core.domain.payment.validation.rules;

import com.lucascosta.desafiopagamento.core.domain.exceptions.UserNotFoundException;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferHandler;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferValidationContext;
import com.lucascosta.desafiopagamento.core.ports.outbound.WalletHolderRepositoryPort;

import static com.lucascosta.desafiopagamento.core.domain.payment.utils.Constants.MSG_PAYEE_NOT_FOUND;

public class LoadPayeeHandler extends TransferHandler {

    private final WalletHolderRepositoryPort repository;

    public LoadPayeeHandler(WalletHolderRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    protected void doHandle(TransferValidationContext ctx) {
        var transfer = ctx.getTransfer();
        var payee = repository.findById(transfer.payeeId());
        if (payee == null) {
            throw new UserNotFoundException(MSG_PAYEE_NOT_FOUND);
        }
        ctx.setPayee(payee);
    }
}
