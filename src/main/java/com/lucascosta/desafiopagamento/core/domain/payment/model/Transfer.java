package com.lucascosta.desafiopagamento.core.domain.payment.model;

import com.lucascosta.desafiopagamento.core.domain.payment.enums.TransferStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class Transfer {

    private Long id;
    private Long payerId;
    private Long payeeId;
    private BigDecimal amount;
    private TransferStatus status;
    private Instant createdAt;
    private Instant finishedAt;
    private String failureReason;

    public Transfer() {
        // Default constructor
    }

    public Transfer(Long id, Long payerId, Long payeeId, BigDecimal amount, TransferStatus status, Instant createdAt, Instant finishedAt, String failureReason) {
        this.id = id;
        this.payerId = payerId;
        this.payeeId = payeeId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
        this.finishedAt = finishedAt;
        this.failureReason = failureReason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
