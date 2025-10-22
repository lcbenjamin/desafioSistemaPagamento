package com.lucascosta.desafiopagamento.adapters.outbound.authorization;

import com.lucascosta.desafiopagamento.core.domain.exceptions.NonRetryableAuthorizationException;
import com.lucascosta.desafiopagamento.core.domain.exceptions.RetryableAuthorizationException;
import com.lucascosta.desafiopagamento.core.domain.exceptions.UnauthorizedTransferException;
import com.lucascosta.desafiopagamento.core.domain.exceptions.UnknownAuthorizationException;
import com.lucascosta.desafiopagamento.core.domain.payment.model.AuthorizationResult;
import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.ports.outbound.http.TransferAuthorizationPort;
import com.lucascosta.desafiopagamento.core.ports.outbound.service.ClientAuthorizationServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.io.IOException;

@Slf4j
@Component("authorizationAdapter")
@RequiredArgsConstructor
public class ClientAuthorizationServiceImpl implements ClientAuthorizationServicePort {

    private final TransferAuthorizationPort authorizationPort;

    @Override
    public void authorizeTransferOrThrowError(Transfer transfer) {
        try {
            AuthorizationResult result = authorizationPort.authorize(transfer);
            if (result == null) {
                throw new UnknownAuthorizationException("Resposta nula do autorizador externo", null);
            }
            if (!result.authorization()) {
                throw new UnauthorizedTransferException("Transferência não autorizada pelo serviço externo.");
            }
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
            handleHttpException((RestClientResponseException) exception);
            return; // handleHttpException já lança exceção específica
        }

        throw new UnknownAuthorizationException("Exceção não mapeada durante autorização", exception);
    }

    private void handleHttpException(RestClientResponseException exception) {
        HttpStatusCode statusCode = exception.getStatusCode();
        String body = exception.getResponseBodyAsString();

        if (statusCode.equals(HttpStatus.FORBIDDEN)) {
            throw new UnauthorizedTransferException("Transferência não autorizada pelo autorizador de transações externo.");
        }

        if (statusCode.is5xxServerError()) {
            throw new RetryableAuthorizationException("Erro interno do serviço de autorização (5xx): " + body, exception);
        }

        if (statusCode.is4xxClientError()) {
            throw new NonRetryableAuthorizationException("Erro de cliente na autorização (4xx): " + body, exception);
        }

        throw new UnknownAuthorizationException("Status HTTP não mapeado: " + statusCode.value(), exception);
    }

    private boolean isNetworkException(Exception exception) {
        return exception instanceof ResourceAccessException || exception instanceof IOException;
    }

    private boolean isHttpException(Exception exception) {
        return exception instanceof RestClientResponseException;
    }

}