package com.lucascosta.desafiopagamento.core.domain.exceptions;

/**
 * Exceção de domínio para erros 4xx (exceto o caso de 403 tratado como resposta de negócio)
 * ao chamar o autorizador externo. Representa um problema de requisição ou contrato
 * e não um erro transitório elegível a retry.
 */
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

