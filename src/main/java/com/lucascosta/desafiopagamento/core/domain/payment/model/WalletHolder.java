package com.lucascosta.desafiopagamento.core.domain.payment.model;

import com.lucascosta.desafiopagamento.core.domain.payment.enums.DocumentType;
import com.lucascosta.desafiopagamento.core.domain.payment.enums.WalletHolderKind;

public record WalletHolder(
        Long id,
        String fullName,
        String document,
        DocumentType documentType,
        String email,
        String passwordHash,
        WalletHolderKind kind,
        Wallet wallet
) {
}