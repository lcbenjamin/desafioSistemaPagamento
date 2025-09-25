package com.lucascosta.desafiopagamento.adapters.outbound.persistence.entity;

import com.lucascosta.desafiopagamento.core.domain.payment.enums.TransferStatus;
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

    @Column(name = "payer_id")
    private Long payerId;

    @Column(name = "payee_id")
    private Long payeeId;

    @Column(name = "amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, columnDefinition = "varchar(20)")
    private TransferStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @Column(name = "failure_reason")
    private String failureReason;
}
