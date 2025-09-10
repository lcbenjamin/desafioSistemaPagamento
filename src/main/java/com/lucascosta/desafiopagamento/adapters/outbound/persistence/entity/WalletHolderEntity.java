package com.lucascosta.desafiopagamento.adapters.outbound.persistence.entity;


import com.lucascosta.desafiopagamento.core.domain.payment.enums.DocumentType;
import com.lucascosta.desafiopagamento.core.domain.payment.enums.WalletHolderKind;
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

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "document", length = 14, nullable = false)
    private String document;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", length = 20, columnDefinition = "varchar(20)")
    private DocumentType documentType;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "kind", length = 20, columnDefinition = "varchar(20)")
    private WalletHolderKind kind;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_wallet_holder_wallet"))
    private WalletEntity wallet;

}
