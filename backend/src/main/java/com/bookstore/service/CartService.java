package com.bookstore.service;

import com.bookstore.dto.cart.AddToCartRequest;
import com.bookstore.dto.cart.CartResponse;

public interface CartService {

    CartResponse addToCart(AddToCartRequest request);

    CartResponse getCart();

    CartResponse updateQuantity(Long bookId, Integer quantity);

    CartResponse removeFromCart(Long bookId);

    void clearCart();
}