package com.example.productDevelopment.dto.projection;

import java.math.BigDecimal;

public interface OrderDetailProjection {
    BigDecimal getAmount();
    String getStatus();
    Long getFulFillmentId();
}
