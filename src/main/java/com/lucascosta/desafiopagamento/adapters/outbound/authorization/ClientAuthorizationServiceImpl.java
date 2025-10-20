package com.lucascosta.desafiopagamento.adapters.outbound.authorization;

import com.lucascosta.desafiopagamento.core.domain.exceptions.NonRetryableAuthorizationException;
import com.lucascosta.desafiopagamento.core.domain.exceptions.RetryableAuthorizationException;
import com.lucascosta.desafiopagamento.core.domain.exceptions.UnauthorizedTransferException;
import com.lucascosta.desafiopagamento.core.domain.exceptions.UnknownAuthorizationException;
import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.ports.outbound.http.TransferAuthorizationPort;
import com.lucascosta.desafiopagamento.core.ports.outbound.service.ClientAuthorizationServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientAuthorizationServiceImpl implements ClientAuthorizationServicePort {

    private final TransferAuthorizationPort authorizationPort;

    @Override
    public void authorizeTransferOrThrowError(Transfer transfer) {
        try {
            authorizationPort.authorize(transfer);
        } catch (Exception e) {
            log.error("Erro ao autorizar transferência: {}", e.getMessage(), e);
            handleAuthorizationException(e);
        }
    }

    private void handleAuthorizationException(Exception exception) {
        if (isNetworkException(exception)) {
            throw new RetryableAuthorizationException("Erro de rede ou timeout durante autorização", exception);
        }

        if (isHttpException(exception)) {
            handleHttpException((WebClientResponseException) exception);
            return;
        }

        throw new UnknownAuthorizationException("Exceção não mapeada durante autorização", exception);
    }


    private void handleHttpException(WebClientResponseException exception) {
        HttpStatusCode statusCode = exception.getStatusCode();

        if (statusCode == HttpStatus.FORBIDDEN) {
            throw new UnauthorizedTransferException("Transferência não autorizada pelo autorizador de transações externo.");
        }

        if (statusCode.is5xxServerError()) {
            throw new RetryableAuthorizationException("Erro interno do serviço de autorização (5xx)", exception);
        }

        if (statusCode.is4xxClientError()) {
            throw new NonRetryableAuthorizationException("Erro de cliente na autorização (4xx)", exception);
        }

        throw new UnknownAuthorizationException("Status HTTP não mapeado: " + statusCode.value(), exception);
    }

    private boolean isNetworkException(Exception exception) {
        return exception instanceof WebClientRequestException || exception instanceof IOException;
    }

    private boolean isHttpException(Exception exception) {
        return exception instanceof WebClientResponseException;
    }

}