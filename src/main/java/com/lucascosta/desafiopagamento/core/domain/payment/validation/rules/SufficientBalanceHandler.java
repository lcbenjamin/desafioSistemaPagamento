package com.lucascosta.desafiopagamento.core.domain.payment.validation.rules;

import com.lucascosta.desafiopagamento.core.domain.exceptions.DomainException;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferHandler;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferValidationContext;
import com.lucascosta.desafiopagamento.core.ports.outbound.WalletRepositoryPort;

import java.math.BigDecimal;

public class SufficientBalanceHandler extends TransferHandler {

    private final WalletRepositoryPort walletRepository;

    public SufficientBalanceHandler(WalletRepositoryPort walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    protected void doHandle(TransferValidationContext ctx) {
        var amount = ctx.getTransfer().amount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("O valor da transferência deve ser maior que zero.");
        }

//        var balance = walletRepository.(ctx.getPayer().getId());
//        if (balance == null || balance.compareTo(amount) < 0) {
//            throw DomainException.of("INSUFFICIENT_FUNDS", "Saldo insuficiente do pagador.");
//        }
    }
}
