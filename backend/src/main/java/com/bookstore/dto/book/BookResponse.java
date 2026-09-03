package com.bookstore.dto.book;

import java.math.BigDecimal;

public record BookResponse(
        Long id,
        String title,
        String author,
        BigDecimal price
) {
}