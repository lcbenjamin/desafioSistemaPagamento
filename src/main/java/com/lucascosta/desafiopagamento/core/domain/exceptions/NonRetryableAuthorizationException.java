package com.lucascosta.desafiopagamento.core.domain.exceptions;

public class NonRetryableAuthorizationException extends RuntimeException {
    public NonRetryableAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
