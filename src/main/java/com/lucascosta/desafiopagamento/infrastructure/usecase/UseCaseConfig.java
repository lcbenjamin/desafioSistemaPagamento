package com.lucascosta.desafiopagamento.infrastructure.usecase;

import com.lucascosta.desafiopagamento.core.application.TransferService;
import com.lucascosta.desafiopagamento.core.ports.inbound.TransferUseCase;
import com.lucascosta.desafiopagamento.core.ports.outbound.TransferAuthorizationPort;
import com.lucascosta.desafiopagamento.core.ports.outbound.WalletHolderRepositoryPort;
import com.lucascosta.desafiopagamento.core.ports.outbound.WalletRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public TransferUseCase transferUseCase(WalletHolderRepositoryPort walletHolderRepository,
                                           WalletRepositoryPort walletRepository,
                                           TransferAuthorizationPort authorizationPort) {
        return new TransferService(walletHolderRepository, walletRepository, authorizationPort);
    }
}
