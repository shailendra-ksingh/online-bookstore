package com.bookstore.entity;

import com.bookstore.exception.InsufficientStockException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void shouldReduceStockWhenQuantityIsAvailable() {
        Book book = new Book(
                "Clean Code",
                "Robert C. Martin",
                new BigDecimal("500.00"),
                10
        );

        book.reduceStock(3);

        assertEquals(7, book.getStock());
    }

    @Test
    void shouldRejectStockReductionWhenQuantityIsNotAvailable() {
        Book book = new Book(
                "Clean Code",
                "Robert C. Martin",
                new BigDecimal("500.00"),
                2
        );

        assertThrows(
                InsufficientStockException.class,
                () -> book.reduceStock(3)
        );

        assertEquals(2, book.getStock());
    }
}
