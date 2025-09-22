package com.lucascosta.desafiopagamento.core.domain.payment.validation.rules;

import com.lucascosta.desafiopagamento.core.domain.exceptions.DomainException;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferHandler;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferValidationContext;

import java.math.BigDecimal;

import static com.lucascosta.desafiopagamento.core.domain.payment.utils.Constants.*;

public class SufficientBalanceHandler extends TransferHandler {

    @Override
    protected void doHandle(TransferValidationContext ctx) {
        var amount = ctx.getTransfer().amount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException(MSG_INVALID_AMOUNT);
        }

        var payer = ctx.getPayer();
        if (payer == null) {
            throw new DomainException(MSG_ERROR_LOAD_PAYER_CTX);
        }

        var wallet = payer.wallet();
        if (wallet == null) {
            throw new DomainException(MSG_ERROR_LOAD_PAYER_CTX);
        }

        var balance = wallet.balance();
        if (balance == null || balance.compareTo(amount) < 0) {
            throw new DomainException(MSG_INSUFFICIENT_FUNDS);
        }
    }
}
