package com.lucascosta.desafiopagamento.infrastructure.resilience;

import com.lucascosta.desafiopagamento.core.domain.payment.model.AuthorizationResult;
import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.ports.outbound.TransferAuthorizationPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class AuthorizationPortWithRetry implements TransferAuthorizationPort {

    private final RetryExecutor retryExecutor;
    private final TransferAuthorizationPort delegate;

    public AuthorizationPortWithRetry(
            RetryExecutor retryExecutor,
            @Qualifier("authorizationAdapter") TransferAuthorizationPort delegate) {
        this.retryExecutor = retryExecutor;
        this.delegate = delegate;
    }

    @Override
    public AuthorizationResult authorize(Transfer transfer) {
        return retryExecutor.execute("authorizationApi", () -> delegate.authorize(transfer));
    }
}