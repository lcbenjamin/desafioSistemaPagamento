package com.lucascosta.desafiopagamento.core.domain.exceptions;

public class ExternalAuthorizationClientException extends DomainException {

    private final int statusCode;
    private final String rawBody;

    public ExternalAuthorizationClientException(int statusCode, String message, String rawBody, Throwable cause) {
        super("Erro cliente autorizador externo (" + statusCode + ") - " + message + (rawBody != null ? " body=" + rawBody : ""));
        if (cause != null) {
            initCause(cause);
        }
        this.statusCode = statusCode;
        this.rawBody = rawBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getRawBody() {
        return rawBody;
    }
}

