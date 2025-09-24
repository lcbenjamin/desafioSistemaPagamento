package com.lucascosta.desafiopagamento.core.domain.utils;

public final class Constants {

    private Constants() {
        // Prevent instantiation
    }

    public static final String MSG_SAME_PARTICIPANT = "O pagador e o recebedor não pode ser a mesma pessoa.";
    public static final String MSG_PAYER_NOT_FOUND = "Pagador não encontrado.";
    public static final String MSG_PAYEE_NOT_FOUND = "Recebedor não encontrado.";
    public static final String MSG_PAYER_CANNOT_BE_MERCHANT = "O pagador não pode ser do tipo 'lojista'.";
    public static final String MSG_INSUFFICIENT_FUNDS = "Saldo insuficiente para realizar a transferência.";
    public static final String MSG_INVALID_AMOUNT = "O valor da transferência deve ser maior que zero.";
    public static final String MSG_EXTERNAL_AUTH_FAILED = "A autorização externa falhou.";
    public static final String MSG_TRANSFER_SUCCESS = "Transferência realizada com sucesso.";
    public static final String MSG_TRANSFER_FAILURE = "Falha ao realizar a transferência.";

    public static final String MSG_ERROR_LOAD_PAYER_CTX = "Pagador não carregado no contexto de validação.";
    public static final String MSG_ERROR_LOAD_WALLET_CTX = "Carteira não carregado no contexto de validação.";

}
