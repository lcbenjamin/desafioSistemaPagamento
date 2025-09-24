package com.lucascosta.desafiopagamento.core.domain.exceptions;

public class InvalidTransferAmountException extends DomainException {
    public InvalidTransferAmountException(String message) {
        super(message);
    }
}
