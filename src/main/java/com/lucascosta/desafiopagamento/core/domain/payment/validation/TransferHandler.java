package com.lucascosta.desafiopagamento.core.domain.payment.validation;

public abstract class TransferHandler {

    private TransferHandler next;

    public TransferHandler linkWith(TransferHandler next) {
        this.next = next;
        return next;
    }

    public final void handle(TransferValidationContext ctx) {
        doHandle(ctx);
        if (next != null) next.handle(ctx);
    }

    protected abstract void doHandle(TransferValidationContext ctx);
}
