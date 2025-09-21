package com.lucascosta.desafiopagamento.core.application;

import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.ports.inbound.TransferResult;
import com.lucascosta.desafiopagamento.core.ports.inbound.TransferUseCase;

public class TransferService implements TransferUseCase {


    @Override
    public TransferResult execute(Transfer transfer) {

        // Valida se o pagador e o recebedor são diferentes
        // Busca e valida pagador
        // Busca e valida recebedor
        // Verifica se o pagador é do tipo "comum", logista nao pode ser pagador
        // Verifica se o pagador tem saldo suficiente


        // valida atraves do sistema externo se a transação pode ser realizada
        // debita o valor do pagador
        // credita o valor no recebedor
        // registra a transação
        // Notifica atraves de sistema externo o pagador que a transação foi realizada com sucesso
        return null;
    }
}
