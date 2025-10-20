package com.lucascosta.desafiopagamento.core.domain.exceptions;

public class UnknownAuthorizationException extends RuntimeException {
    public UnknownAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
