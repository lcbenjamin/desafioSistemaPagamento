package com.lucascosta.desafiopagamento.adapters.outbound.persistence.mappers;

import com.lucascosta.desafiopagamento.adapters.outbound.persistence.entity.WalletEntity;
import com.lucascosta.desafiopagamento.core.domain.payment.model.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    Wallet toModel(WalletEntity entity);

    WalletEntity toEntity(Wallet model);
}
