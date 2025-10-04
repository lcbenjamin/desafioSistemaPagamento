package com.lucascosta.desafiopagamento.core.domain.exceptions;

public final class WalletNotFoundException extends DomainException {
    public WalletNotFoundException(String message) {
        super(message);
    }
}
