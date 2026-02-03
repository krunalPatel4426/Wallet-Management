package com.example.productDevelopment.repository;

import com.example.productDevelopment.entity.transaction.TransactionEntity;
import com.example.productDevelopment.enums.TransactionTypeEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    INSERT INTO transactions(client_id, message, transaction_type) values (:clientId, :message, :trasactionType);
""")
    public int addTransaction(Long clientId, String message, String trasactionType);
}
