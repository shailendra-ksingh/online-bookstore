package com.bookstore.dto.cart;

import java.math.BigDecimal;

public record CartItemResponse(
        Long bookId,
        String title,
        BigDecimal price,
        Integer quantity,
        BigDecimal itemTotal
) {
}