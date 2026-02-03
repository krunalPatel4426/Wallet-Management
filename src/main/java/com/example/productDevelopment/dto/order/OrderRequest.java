package com.example.productDevelopment.dto.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequest {
    private BigDecimal amount;
}
