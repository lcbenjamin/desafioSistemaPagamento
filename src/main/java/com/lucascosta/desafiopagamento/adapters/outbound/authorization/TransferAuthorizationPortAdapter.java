package com.lucascosta.desafiopagamento.adapters.outbound.authorization;

import com.lucascosta.desafiopagamento.adapters.outbound.mappers.AuthorizationResponseMapper;
import com.lucascosta.desafiopagamento.core.domain.payment.model.AuthorizationResult;
import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import com.lucascosta.desafiopagamento.core.ports.outbound.TransferAuthorizationPort;
import com.lucascosta.desafiopagamento.infrastructure.config.AuthorizationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;

import java.io.IOException;

@Component("authorizationAdapter")
@RequiredArgsConstructor
@Slf4j
public class TransferAuthorizationPortAdapter implements TransferAuthorizationPort {

    private final RestClient authorizationRestClient;
    private final AuthorizationProperties props;
    private final AuthorizationResponseMapper mapper;

    @Override
    public AuthorizationResult authorize(Transfer transfer) {
        return authorizationRestClient.get()
                .uri(props.path())
                .exchange((request, response)
                        -> getAuthorizationResult(response)
                );
    }

    private AuthorizationResult getAuthorizationResult(ConvertibleClientHttpResponse response) throws IOException {
        if (response.getStatusCode() == HttpStatus.FORBIDDEN) {
            log.info("Transferência não autorizada pelo autorizador de transações.");
            return new AuthorizationResult("NEGADO", false);
        }
        return mapper.toDomain(response.bodyTo(AuthorizationApiResponse.class));
    }

}