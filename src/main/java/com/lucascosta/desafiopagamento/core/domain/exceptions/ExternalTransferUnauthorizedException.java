package com.lucascosta.desafiopagamento.core.domain.exceptions;

public class ExternalTransferUnauthorizedException extends DomainException {
    public ExternalTransferUnauthorizedException(String message) {
        super(message);
    }
}
