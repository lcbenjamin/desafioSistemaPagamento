package com.lucascosta.desafiopagamento.core.domain.payment.model;

import com.lucascosta.desafiopagamento.core.domain.payment.enums.DocumentType;
import com.lucascosta.desafiopagamento.core.domain.payment.enums.WalletHolderKind;

public class WalletHolder {

    private Long id;
    private String fullName;
    private String document;
    private DocumentType documentType;
    private String email;
    private String passwordHash;
    private WalletHolderKind kind;
    private Wallet wallet;

    public WalletHolder() {
        // Default constructor
    }

    public WalletHolder(Long id, String fullName, String document, DocumentType documentType, String email, String passwordHash, Wallet wallet) {
        this.id = id;
        this.fullName = fullName;
        this.document = document;
        this.documentType = documentType;
        this.email = email;
        this.passwordHash = passwordHash;
        this.kind = WalletHolderKind.COMMON;
        this.wallet = wallet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public WalletHolderKind getKind() {
        return kind;
    }
    public void setKind(WalletHolderKind kind) {
        this.kind = kind;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
