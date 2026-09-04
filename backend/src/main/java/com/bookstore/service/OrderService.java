package com.bookstore.service;

import com.bookstore.dto.cart.CartResponse;
import com.bookstore.dto.order.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;

    // Using an in-memory sequence for order IDs in this assignment.
    // Normally, orders and their IDs would come from the database.
    private final AtomicLong orderSequence = new AtomicLong(1000);

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

        // Order is created, so clear the current cart.
        cartService.clearCart();

        return orderResponse;
    }
}