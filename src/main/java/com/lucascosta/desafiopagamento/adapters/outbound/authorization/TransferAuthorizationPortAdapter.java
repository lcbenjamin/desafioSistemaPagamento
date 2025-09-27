package com.lucascosta.desafiopagamento.adapters.outbound.authorization;

import com.lucascosta.desafiopagamento.adapters.outbound.mappers.AuthorizationResponseMapper;
import com.lucascosta.desafiopagamento.core.domain.exceptions.ExternalAuthorizationClientException;
import com.lucascosta.desafiopagamento.core.domain.exceptions.ExternalAuthorizationCommunicationException;
import com.lucascosta.desafiopagamento.core.domain.payment.model.AuthorizationResult;
import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.ports.outbound.TransferAuthorizationPort;
import com.lucascosta.desafiopagamento.infrastructure.config.AuthorizationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component("authorizationAdapter")
@RequiredArgsConstructor
@Slf4j
public class TransferAuthorizationPortAdapter implements TransferAuthorizationPort {

    private final RestClient authorizationRestClient;
    private final AuthorizationProperties props;
    private final AuthorizationResponseMapper mapper;

    @Override
    public AuthorizationResult authorize(Transfer transfer) {
        try {
            var response = authorizationRestClient.get()
                    .uri(props.path())
                    .retrieve()
                    .body(AuthorizationApiResponse.class);

            return mapper.toDomain(response);

        } catch (HttpClientErrorException ex) {
            return getAuthorizationResult(ex);
        } catch (RestClientException ex) {
            throw new ExternalAuthorizationCommunicationException("Falha ao comunicar com autorizador externo", ex);
        }
    }

    @NotNull
    private static AuthorizationResult getAuthorizationResult(HttpClientErrorException ex) {
        if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
            log.info("Autorização negada pelo autorizador externo (403).");
            return new AuthorizationResult("fail", false);
        }
        throw new ExternalAuthorizationClientException(
                ex.getStatusCode().value(),
                ex.getStatusText(),
                ex.getResponseBodyAsString(),
                ex
        );
    }
}