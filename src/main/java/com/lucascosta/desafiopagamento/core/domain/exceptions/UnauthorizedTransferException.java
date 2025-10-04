package com.lucascosta.desafiopagamento.core.domain.exceptions;

public final class UnauthorizedTransferException extends DomainException {
    public UnauthorizedTransferException(String message) {
        super(message);
    }
}
