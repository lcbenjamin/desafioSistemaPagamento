package com.lucascosta.desafiopagamento.adapters.inbound.controller;

import com.lucascosta.desafiopagamento.adapters.inbound.dto.TransferRequest;
import com.lucascosta.desafiopagamento.adapters.inbound.mapper.TransferDTOMapper;
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
@RequestMapping("/transfer")
public class TransferController {

    private final TransferUseCase transferService;
    private final TransferDTOMapper mapper;

    @PostMapping
    public ResponseEntity<String> createTransfer(@RequestBody @Valid TransferRequest request) {
        var transfer = mapper.toModel(request);
        transferService.execute(transfer);
        return ResponseEntity.ok("OK");
    }

}
