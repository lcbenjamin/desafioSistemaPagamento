package com.lucascosta.desafiopagamento.adapters.persistence.entity;

import com.lucascosta.desafiopagamento.domain.model.enums.TransferStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transfers")
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long payerId;

    private Long payeeId;

    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    private Instant createdAt;

    private Instant finishedAt;

    private String failureReason;
}
