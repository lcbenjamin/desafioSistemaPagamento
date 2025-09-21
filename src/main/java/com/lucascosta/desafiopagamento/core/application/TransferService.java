package com.lucascosta.desafiopagamento.core.application;

import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.ports.inbound.TransferResult;
import com.lucascosta.desafiopagamento.core.ports.inbound.TransferUseCase;
import org.springframework.stereotype.Service;

@Service // Unica referencia ao Spring no core, para ser injetado em outros lugares
public class TransferService implements TransferUseCase {


    @Override
    public TransferResult execute(Transfer transfer) {

        var line = "=".repeat(30);
        System.out.println(line);
        System.out.println("Iniciando transferência");
        System.out.println("De: " + transfer.amount());
        System.out.println("Para: " + transfer.payerId());
        System.out.println("Valor: " + transfer.payeeId());
        System.out.println(line);

        // TODO: Valida se o pagador e o recebedor são diferentes
        // TODO: Busca e valida pagador
        // TODO: Busca e valida recebedor
        // TODO: Verifica se o pagador é do tipo "comum", logista nao pode ser pagador
        // TODO: Verifica se o pagador tem saldo suficiente


        // TODO: valida atraves do sistema externo se a transação pode ser realizada
        // TODO: debita o valor do pagador
        // TODO: credita o valor no recebedor
        // TODO: registra a transação
        // TODO: Notifica atraves de sistema externo o pagador que a transação foi realizada com sucesso
        return null;
    }
}
