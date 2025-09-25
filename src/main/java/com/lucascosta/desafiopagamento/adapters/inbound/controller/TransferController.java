package com.lucascosta.desafiopagamento.adapters.inbound.controller;

import com.lucascosta.desafiopagamento.adapters.inbound.dto.TransferRequest;
import com.lucascosta.desafiopagamento.adapters.inbound.mapper.TransferDTOMapper;
import com.lucascosta.desafiopagamento.core.domain.payment.model.TransferResult;
import com.lucascosta.desafiopagamento.core.ports.inbound.TransferUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/transfers")
public class TransferController {

    private final TransferUseCase transferService;
    private final TransferDTOMapper mapper;

    @PostMapping
    public ResponseEntity<TransferResult> executeTransfer(@RequestBody @Valid TransferRequest request) {
        var transfer = mapper.toModel(request);
        var response = transferService.execute(transfer);
        return ResponseEntity.ok(response);
    }

}
