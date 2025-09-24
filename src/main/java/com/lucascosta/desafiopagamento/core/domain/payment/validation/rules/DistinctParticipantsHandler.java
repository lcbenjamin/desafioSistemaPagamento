package com.lucascosta.desafiopagamento.core.domain.payment.validation.rules;

import com.lucascosta.desafiopagamento.core.domain.exceptions.ValidationException;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferHandler;
import com.lucascosta.desafiopagamento.core.domain.payment.validation.TransferValidationContext;

import static com.lucascosta.desafiopagamento.core.domain.utils.Constants.MSG_SAME_PARTICIPANT;

public class DistinctParticipantsHandler extends TransferHandler {

    @Override
    protected void doHandle(TransferValidationContext ctx) {
        var transfer = ctx.getTransfer();
        if (transfer.payeeId().equals(transfer.payerId())) {
            throw new ValidationException(MSG_SAME_PARTICIPANT);
        }
    }
}
