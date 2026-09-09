package com.bookstore.service.impl;

import com.bookstore.dto.cart.CartItemResponse;
import com.bookstore.dto.cart.CartResponse;
import com.bookstore.dto.order.OrderResponse;
import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import com.bookstore.repository.OrderRepository;
import com.bookstore.service.CartService;
import com.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponse createOrder() {

        CartResponse cart = cartService.getCart();

        if (cart.items().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot create order because cart is empty"
            );
        }

        Order order = Order.builder()
                .createdAt(LocalDateTime.now())
                .totalAmount(cart.total())
                .build();

        for (CartItemResponse cartItem : cart.items()) {

            OrderItem orderItem = OrderItem.builder()
                    .bookId(cartItem.bookId())
                    .title(cartItem.title())
                    .price(cartItem.price())
                    .quantity(cartItem.quantity())
                    .itemTotal(cartItem.itemTotal())
                    .build();

            order.addItem(orderItem);
        }

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart();

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getTotalAmount(),
                "CONFIRMED"
        );
    }
}