package com.example.productDevelopment.entity.client;

import com.example.productDevelopment.entity.order.OrderEntity;
import com.example.productDevelopment.entity.transaction.TransactionEntity;
import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clients")
@Data
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "username")
    private String username;

    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

    @Version
    @Column(name = "version")
    private Long version;



    @Column(name = "is_admin")
    private int isAdmin;

    @OneToMany(mappedBy = "client")
    private List<OrderEntity> order;

    @OneToMany(mappedBy = "client")
    private List<TransactionEntity> transaction;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
