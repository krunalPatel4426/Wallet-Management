package com.example.productDevelopment.dto.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailResponse {
    private Long fulfillmentId;
    private String status;
    private BigDecimal amount;
    private boolean success;
}
