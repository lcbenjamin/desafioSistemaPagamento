package com.lucascosta.desafiopagamento.infrastructure.resilience;

import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.ports.outbound.service.ClientAuthorizationServicePort;
import io.github.resilience4j.retry.Retry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class TransferAuthorizationWithRetry implements ClientAuthorizationServicePort {

    private final RetryExecutor retryExecutor;
    private final ClientAuthorizationServicePort delegate;
    private final Retry authorizationApiRetry;

    public TransferAuthorizationWithRetry(
            RetryExecutor retryExecutor,
            @Qualifier("authorizationAdapter") ClientAuthorizationServicePort delegate,
            @Qualifier("authorizationApiRetry") Retry authorizationApiRetry) {
        this.retryExecutor = retryExecutor;
        this.delegate = delegate;
        this.authorizationApiRetry = authorizationApiRetry;
    }

    @Override
    public void authorizeTransferOrThrowError(Transfer transfer) {
        retryExecutor.execute(authorizationApiRetry, (Runnable) () -> delegate.authorizeTransferOrThrowError(transfer));
    }
}