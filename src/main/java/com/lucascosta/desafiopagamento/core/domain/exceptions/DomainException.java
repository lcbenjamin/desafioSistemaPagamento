package com.lucascosta.desafiopagamento.core.domain.exceptions;

sealed class DomainException extends RuntimeException permits  InsufficientFundsException, InvalidTransferAmountException, UnauthorizedTransferException, UserNotFoundException, ValidationException, WalletNotFoundException {
    public DomainException(String message) {
        super(message);
    }
}
