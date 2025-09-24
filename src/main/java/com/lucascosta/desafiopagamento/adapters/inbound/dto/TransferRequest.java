package com.lucascosta.desafiopagamento.adapters.inbound.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(

        @NotNull(message = "O ID do pagador é obrigatório")
        Long payerId,

        @NotNull(message = "O ID do recebedor é obrigatório")
        Long payeeId,

        @Positive(message = "O valor da transferência deve ser positivo")
        @Digits(integer = 10, fraction = 2, message = "O valor da transferência deve ter no máximo 10 dígitos inteiros e 2 decimais")
        @NotNull(message = "O valor da transferência é obrigatório")
        BigDecimal value
) {
}
