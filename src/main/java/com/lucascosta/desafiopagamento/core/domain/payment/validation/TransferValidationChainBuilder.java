package com.lucascosta.desafiopagamento.core.domain.payment.validation;

import java.util.Objects;

public final class TransferValidationChainBuilder {

    private TransferHandler head;
    private TransferHandler tail;

    private TransferValidationChainBuilder() { }

    public static TransferValidationChainBuilder builder() {
        return new TransferValidationChainBuilder();
    }

    public TransferValidationChainBuilder add(TransferHandler handler) {
        Objects.requireNonNull(handler, "handler não pode ser nulo");
        if (head == null) {
            head = handler;
            tail = handler;
        } else {
            tail.linkWith(handler);
            tail = handler;
        }
        return this;
    }

    public TransferHandler build() {
        if (head == null) {
            throw new IllegalStateException("Nenhum handler foi adicionado à cadeia de validação.");
        }
        return head;
    }
}

