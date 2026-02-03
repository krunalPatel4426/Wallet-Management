package com.example.productDevelopment.repository;

import com.example.productDevelopment.dto.admin.AdminTransactionRequest;
import com.example.productDevelopment.entity.client.ClientEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
        UPDATE clients SET balance = balance + :amount where client_id = :id
""")
    public int creditWallet(Long id, BigDecimal amount);

    @Query(nativeQuery = true, value = """
    SELECT c.version from clients c where c.client_id = :id;
""")
    public Long getCurrentVersion(Long id);

    @Query(nativeQuery = true, value = """
    SELECT c.balance from clients c where c.client_id = :id;
""")
    public BigDecimal getBalance(Long id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    update clients set balance = balance - :amount, version = version + 1 
        where balance >= :amount and client_id = :id and version = :version
""")
    public int debitAmount(Long id, BigDecimal amount, Long version);

}
