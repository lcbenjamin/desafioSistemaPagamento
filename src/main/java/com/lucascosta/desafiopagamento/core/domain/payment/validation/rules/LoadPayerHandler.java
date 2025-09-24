package com.lucascosta.desafiopagamento.core.domain.payment.validation.rules;

import com.lucascosta.desafiopagamento.core.domain.exceptions.UserNotFoundException;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferHandler;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferValidationContext;
import com.lucascosta.desafiopagamento.core.ports.outbound.WalletHolderRepositoryPort;

import static com.lucascosta.desafiopagamento.core.domain.utils.Constants.MSG_PAYER_NOT_FOUND;

public class LoadPayerHandler extends TransferHandler {

    private final WalletHolderRepositoryPort repository;

    public LoadPayerHandler(WalletHolderRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    protected void doHandle(TransferValidationContext ctx) {
        var transfer = ctx.getTransfer();
        var payer = repository.findById(transfer.payerId());
        if (payer == null) {
            throw new UserNotFoundException(MSG_PAYER_NOT_FOUND);
        }
        ctx.setPayer(payer);
    }
}