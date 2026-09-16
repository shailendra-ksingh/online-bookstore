package com.bookstore.dto.order;

import com.bookstore.entity.OrderStatus;

import java.math.BigDecimal;

public record OrderResponse(
        Long orderId,
        BigDecimal total,
        OrderStatus status
) {
}