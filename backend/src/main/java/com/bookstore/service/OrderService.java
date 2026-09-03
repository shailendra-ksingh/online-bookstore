package com.bookstore.service;

import com.bookstore.dto.cart.CartResponse;
import com.bookstore.dto.order.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private final CartService cartService;

    /*
     * Simple in-memory order ID generation for this assignment.
     * A production application would persist orders and use a database-generated ID.
     */
    private final AtomicLong orderSequence = new AtomicLong(1000);

    public OrderService(CartService cartService) {
        this.cartService = cartService;
    }

    public OrderResponse createOrder() {

        CartResponse cart = cartService.getCart();

        if (cart.items().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot create order because cart is empty"
            );
        }

        Long orderId = orderSequence.incrementAndGet();

        OrderResponse orderResponse = new OrderResponse(
                orderId,
                cart.total(),
                "CONFIRMED"
        );

        // Clear cart after successful order creation.
        cartService.clearCart();

        return orderResponse;
    }
}