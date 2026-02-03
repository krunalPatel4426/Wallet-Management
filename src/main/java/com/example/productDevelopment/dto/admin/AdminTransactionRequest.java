package com.example.productDevelopment.dto.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminTransactionRequest {
    private Long clientId;
    private BigDecimal amount;
}
