package com.bookstore.exception;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(Long bookId) {
        super("Book not found in cart: " + bookId);
    }
}