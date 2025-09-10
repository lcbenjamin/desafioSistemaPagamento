package com.lucascosta.desafiopagamento.util.mappers;

import com.lucascosta.desafiopagamento.adapters.outbound.persistence.entity.WalletHolderEntity;
import com.lucascosta.desafiopagamento.core.domain.payment.model.WalletHolder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletHolderMapper {

    WalletHolder toModel(WalletHolderEntity entity);

    WalletHolderEntity toEntity(WalletHolder model);

}
