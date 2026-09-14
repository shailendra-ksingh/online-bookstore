package com.bookstore.exception;

public class InsufficientStockException
        extends RuntimeException {

    public InsufficientStockException(
            String title,
            int available) {

        super(
                "Insufficient stock for book: "
                        + title
                        + ". Available: "
                        + available
        );
    }
}
