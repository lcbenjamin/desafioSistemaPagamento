package com.lucascosta.desafiopagamento.core.domain.payment.utils;

public class Constants {

    public static final String  MSG_SAME_PARTICIPANT = "O pagador e o recebedor devem ser diferentes.";
    public static final String  MSG_PAYER_NOT_FOUND = "Pagador não encontrado.";
    public static final String  MSG_PAYEE_NOT_FOUND = "Recebedor não encontrado.";
    public static final String  MSG_PAYER_CANNOT_BE_MERCHANT = "O pagador não pode ser do tipo 'lojista'.";
    public static final String  MSG_INSUFFICIENT_FUNDS = "Saldo insuficiente para realizar a transferência.";
    public static final String  MSG_EXTERNAL_AUTH_FAILED = "A autorização externa falhou.";
    public static final String  MSG_TRANSFER_SUCCESS = "Transferência realizada com sucesso.";
    public static final String  MSG_TRANSFER_FAILURE = "Falha ao realizar a transferência.";

    public static final String MSG_ERROR_LOAD_PAYER_CTX = "Pagador não carregado no contexto de validação.";

}
