package com.lucascosta.desafiopagamento.core.domain.exceptions;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
