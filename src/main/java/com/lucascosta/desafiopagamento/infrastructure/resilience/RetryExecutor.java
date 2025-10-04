package com.lucascosta.desafiopagamento.infrastructure.resilience;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryExecutor {

    private final RetryRegistry retryRegistry;

    @PostConstruct
    void attachLoggingToAllRetries() {
        retryRegistry.getAllRetries().forEach(this::attachLogging);
        retryRegistry.getEventPublisher().onEntryAdded(e -> attachLogging(e.getAddedEntry()));
    }

    private void attachLogging(Retry retry) {
        retry.getEventPublisher()
                .onRetry(event -> log.warn("Retry attempt {} for {}", event.getNumberOfRetryAttempts(), event.getName()))
                .onError(event -> log.error("Failed after {} retries for {}", event.getNumberOfRetryAttempts(), event.getName(), event.getLastThrowable()));
    }

    public <T> T execute(Retry retry, Supplier<T> action) {
        Objects.requireNonNull(retry, "Retry não pode ser nulo");
        Supplier<T> decorated = Retry.decorateSupplier(retry, action);
        return decorated.get();
    }

    public void execute(Retry retry, Runnable action) {
        Objects.requireNonNull(retry, "Retry não pode ser nulo");
        Runnable decorated = Retry.decorateRunnable(retry, action);
        decorated.run();
    }

    public <T> T execute(String retryName, Supplier<T> action) {
        Retry retry = retryRegistry.retry(retryName);
        return execute(retry, action);
    }


}