package com.bookstore.service.impl;

import com.bookstore.dto.cart.CartItemResponse;
import com.bookstore.dto.cart.CartResponse;
import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import com.bookstore.entity.OrderStatus;

import java.time.LocalDateTime;

public class OrderFactory {

    private OrderFactory() {  }

    public static Order createOrder(CartResponse cart) {

        Order order = Order.builder()
                .createdAt(LocalDateTime.now())
                .totalAmount(cart.total())
                .status(OrderStatus.CONFIRMED)
                .build();

        for (CartItemResponse item : cart.items()) {
            OrderItem orderItem = OrderItem.builder()
                    .bookId(item.bookId())
                    .title(item.title())
                    .price(item.price())
                    .quantity(item.quantity())
                    .itemTotal(item.itemTotal())
                    .build();

            order.addItem(orderItem);
        }

        return order;
    }
}
