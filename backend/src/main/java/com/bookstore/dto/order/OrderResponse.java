package com.bookstore.dto.order;

import java.math.BigDecimal;

public record OrderResponse(
        Long orderId,
        BigDecimal total,
        String status
) {
}