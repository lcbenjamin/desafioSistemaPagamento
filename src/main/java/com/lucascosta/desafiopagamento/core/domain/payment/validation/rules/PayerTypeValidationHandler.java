package com.lucascosta.desafiopagamento.core.domain.payment.validation.rules;

import com.lucascosta.desafiopagamento.core.domain.exceptions.DomainException;
import com.lucascosta.desafiopagamento.core.domain.payment.enums.WalletHolderKind;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferHandler;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferValidationContext;

import static com.lucascosta.desafiopagamento.core.domain.payment.utils.Constants.MSG_ERROR_LOAD_PAYER_CTX;
import static com.lucascosta.desafiopagamento.core.domain.payment.utils.Constants.MSG_PAYER_CANNOT_BE_MERCHANT;

public class PayerTypeValidationHandler extends TransferHandler {

    @Override
    protected void doHandle(TransferValidationContext ctx) {
        var payer = ctx.getPayer();
        if (payer == null) {
            throw new RuntimeException(MSG_ERROR_LOAD_PAYER_CTX);
        }
        if (payer.kind().equals(WalletHolderKind.MERCHANT)) {
            throw new DomainException(MSG_PAYER_CANNOT_BE_MERCHANT);
        }
    }
}
