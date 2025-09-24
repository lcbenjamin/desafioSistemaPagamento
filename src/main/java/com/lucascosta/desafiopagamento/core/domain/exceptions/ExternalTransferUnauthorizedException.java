package com.lucascosta.desafiopagamento.core.domain.exceptions;

public class ExternalTransferUnauthorizedException extends RuntimeException {
    public ExternalTransferUnauthorizedException(String message) {
        super(message);
    }
}
