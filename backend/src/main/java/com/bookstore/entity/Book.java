package com.bookstore.entity;

import com.bookstore.exception.InsufficientStockException;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "books")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank(message = "Author is required")
    @Column(nullable = false, length = 100)
    private String author;

    @NotNull(message = "Price is required")
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Price must be greater than zero"
    )
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull(message = "Stock is required")
    @PositiveOrZero(message = "Stock cannot be negative")
    @Column(nullable = false)
    private Integer stock;

    public Book(String title, String author, BigDecimal price, int stock) {
        this.title = title.trim();
        this.author = author.trim();
        this.price = price;
        this.stock = stock;
    }

    public void reduceStock(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero"
            );
        }

        if (quantity > stock) {
            throw new InsufficientStockException(
                    title,
                    stock
            );
        }

        stock -= quantity;
    }

    public boolean hasEnoughStock(int quantity) {
        return quantity > 0 && quantity <= stock;
    }
}