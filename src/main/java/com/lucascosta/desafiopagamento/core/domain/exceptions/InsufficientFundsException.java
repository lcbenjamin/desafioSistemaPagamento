package com.lucascosta.desafiopagamento.core.domain.exceptions;

public final class InsufficientFundsException extends DomainException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
