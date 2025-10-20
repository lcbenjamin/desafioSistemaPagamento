package com.lucascosta.desafiopagamento.core.domain.exceptions;

public class RetryableAuthorizationException extends RuntimeException {
    public RetryableAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
