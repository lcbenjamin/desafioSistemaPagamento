package com.lucascosta.desafiopagamento.adapters.inbound.mapper;

import com.lucascosta.desafiopagamento.adapters.inbound.dto.TransferRequest;
import com.lucascosta.desafiopagamento.core.domain.payment.model.Transfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransferDTOMapper {

    @Mapping(source = "amount", target = "value")
    TransferRequest toDTO(Transfer transfer);

    @Mapping(source = "value", target = "amount")
    Transfer toModel(TransferRequest request);

}
