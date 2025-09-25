package com.lucascosta.desafiopagamento.adapters.outbound.authorization;

import com.lucascosta.desafiopagamento.core.domain.exceptions.ExternalAuthorizationClientException;
import com.lucascosta.desafiopagamento.core.domain.exceptions.ExternalAuthorizationCommunicationException;
import com.lucascosta.desafiopagamento.core.domain.payment.model.AuthorizationResult;
import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.ports.outbound.TransferAuthorizationPort;
import com.lucascosta.desafiopagamento.infrastructure.config.AuthorizationProperties;
import io.github.resilience4j.retry.Retry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferAuthorizationPortAdapter implements TransferAuthorizationPort {

    private final RestClient authorizationRestClient;
    private final AuthorizationProperties props;
    private final Retry authorizationApiRetry;

    @PostConstruct
    void configureRetryEvents() {
        authorizationApiRetry.getEventPublisher()
                .onRetry(event -> log.warn("Retry attempt {} for {}", event.getNumberOfRetryAttempts(), event.getName()))
                .onError(event -> log.error("Failed after {} retries", event.getNumberOfRetryAttempts(), event.getLastThrowable()));
    }

    @Override
    public AuthorizationResult authorize(Transfer transfer) {
        Supplier<AuthorizationResult> supplier = Retry.decorateSupplier(authorizationApiRetry, () -> doAuthorize(transfer));
        try {
            return supplier.get();
        } catch (HttpClientErrorException ex) {

            if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                log.info("Autorização negada pelo autorizador externo (403). Retornando authorization=false.");
                return new AuthorizationResult("fail", false);
            }
            throw new ExternalAuthorizationClientException(
                    ex.getStatusCode().value(),
                    ex.getStatusText(),
                    safeBody(ex.getResponseBodyAsString()),
                    ex
            );
        } catch (ResourceAccessException | HttpServerErrorException ex) {
            throw new ExternalAuthorizationCommunicationException(buildCommFailureMessage(ex), ex);
        }
    }

    private String buildCommFailureMessage(Throwable ex) {
        var metrics = authorizationApiRetry.getMetrics();
        long failedWithRetry = metrics.getNumberOfFailedCallsWithRetryAttempt();
        if (failedWithRetry > 0) {
            return "Falha após tentativas com retry ao comunicar com autorizador externo";
        }
        return "Falha (sem retries elegíveis) ao comunicar com autorizador externo";
    }

    private String safeBody(String body) {
        if (body == null) return null;
        if (body.length() > 500) {
            return body.substring(0, 500) + "...";
        }
        return body;
    }

    private AuthorizationResult doAuthorize(Transfer transfer) {
        var apiResponse = authorizationRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(props.path())
                        .build())
                .retrieve()
                .body(AuthorizationApiResponse.class);

        return mapToDomain(apiResponse);
    }

    private AuthorizationResult mapToDomain(AuthorizationApiResponse response) {
        if (response == null) {
            throw new IllegalStateException("Resposta nula do autorizador externo");
        }
        var data = Objects.requireNonNull(response.data(), "Campo 'data' ausente na resposta");
        return new AuthorizationResult(response.status(), data.authorization());
    }
}