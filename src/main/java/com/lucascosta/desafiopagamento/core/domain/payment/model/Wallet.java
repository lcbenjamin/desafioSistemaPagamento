package com.lucascosta.desafiopagamento.core.domain.payment.model;

import java.math.BigDecimal;

public class Wallet {

    private Long id;
    private Long version;
    private BigDecimal balance;

    public Wallet() {
        // Default constructor
    }

    public Wallet(Long id, Long version, BigDecimal balance) {
        this.id = id;
        this.version = version;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
