package com.lucascosta.desafiopagamento.adapters.outbound.persistence.mappers;

import com.lucascosta.desafiopagamento.adapters.outbound.persistence.entity.TransferEntity;
import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransferEntityMapper {

    Transfer toModel(TransferEntity entity);

    TransferEntity toEntity(Transfer model);

}

