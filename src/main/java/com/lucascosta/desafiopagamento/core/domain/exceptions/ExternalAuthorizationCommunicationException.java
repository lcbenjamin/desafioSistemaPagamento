package com.lucascosta.desafiopagamento.core.domain.exceptions;


public class ExternalAuthorizationCommunicationException extends RuntimeException {
    public ExternalAuthorizationCommunicationException(String message, Throwable cause) {
        super(message + (cause != null ? ": " + cause.getMessage() : ""));
        if (cause != null) {
            this.initCause(cause);
        }
    }
}

