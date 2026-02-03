package com.example.productDevelopment.entity.transaction;

import com.example.productDevelopment.entity.client.ClientEntity;
import com.example.productDevelopment.enums.TransactionTypeEnum;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum transactionType;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
