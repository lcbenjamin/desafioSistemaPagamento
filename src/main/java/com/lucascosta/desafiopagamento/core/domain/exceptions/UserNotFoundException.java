package com.lucascosta.desafiopagamento.core.domain.exceptions;

public final class UserNotFoundException extends DomainException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
