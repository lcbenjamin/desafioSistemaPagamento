package com.lucascosta.desafiopagamento.adapters.persistence.entity;


import com.lucascosta.desafiopagamento.domain.model.enums.DocumentType;
import com.lucascosta.desafiopagamento.domain.model.enums.HolderKind;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wallet_holders", uniqueConstraints = {
        @UniqueConstraint(columnNames = "document"),
        @UniqueConstraint(columnNames = "email")
})
public class WalletHolderEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String document;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private String email;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private HolderKind kind;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="wallet_id")
    private WalletEntity wallet;



}
