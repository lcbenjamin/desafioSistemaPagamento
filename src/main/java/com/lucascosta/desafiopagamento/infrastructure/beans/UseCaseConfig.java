package com.lucascosta.desafiopagamento.infrastructure.beans;

import com.lucascosta.desafiopagamento.core.application.TransferService;
import com.lucascosta.desafiopagamento.core.ports.inbound.TransferUseCase;
import com.lucascosta.desafiopagamento.core.ports.outbound.persistence.WalletHolderRepositoryPort;
import com.lucascosta.desafiopagamento.core.ports.outbound.persistence.WalletRepositoryPort;
import com.lucascosta.desafiopagamento.core.ports.outbound.service.ClientAuthorizationServicePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public TransferUseCase transferUseCase(WalletHolderRepositoryPort walletHolderRepository,
                                           WalletRepositoryPort walletRepository,
                                           ClientAuthorizationServicePort authorizationPort) {
        return new TransferService(walletHolderRepository, walletRepository, authorizationPort);
    }
}
