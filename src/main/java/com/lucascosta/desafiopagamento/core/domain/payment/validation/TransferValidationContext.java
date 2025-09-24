package com.lucascosta.desafiopagamento.core.domain.payment.validation;

import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.domain.payment.model.WalletHolder;

public class TransferValidationContext {

    private final Transfer transfer;
    private WalletHolder payer;
    private WalletHolder payee;

    public TransferValidationContext(Transfer transfer) {
        this.transfer = transfer;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public WalletHolder getPayer() {
        return payer;
    }

    public void setPayer(WalletHolder payer) {
        this.payer = payer;
    }

    public WalletHolder getPayee() {
        return payee;
    }

    public void setPayee(WalletHolder payee) {
        this.payee = payee;
    }
}
