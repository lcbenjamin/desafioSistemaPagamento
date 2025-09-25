package com.lucascosta.desafiopagamento.core.domain.exceptions;

/**
 * Exceção de domínio para falha de comunicação (após esgotar retries) com o serviço externo de autorização.
 */
public class ExternalAuthorizationCommunicationException extends DomainException {
    public ExternalAuthorizationCommunicationException(String message, Throwable cause) {
        super(message + (cause != null ? ": " + cause.getMessage() : ""));
        if (cause != null) {
            this.initCause(cause);
        }
    }
}

