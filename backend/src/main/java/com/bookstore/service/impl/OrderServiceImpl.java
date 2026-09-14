package com.bookstore.service.impl;

import com.bookstore.dto.cart.CartItemResponse;
import com.bookstore.dto.cart.CartResponse;
import com.bookstore.dto.order.OrderResponse;
import com.bookstore.entity.Book;
import com.bookstore.entity.Order;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.InsufficientStockException;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.OrderRepository;
import com.bookstore.service.CartService;
import com.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    @Override
    @Transactional
    public OrderResponse createOrder() {

        CartResponse cart = cartService.getCart();

        if (cart.items().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot create order because cart is empty"
            );
        }

        for (CartItemResponse cartItem : cart.items()) {

            Book book = bookRepository.findById(cartItem.bookId())
                    .orElseThrow(() ->
                            new BookNotFoundException(cartItem.bookId()));

            if (!book.hasEnoughStock(cartItem.quantity())) {
                throw new InsufficientStockException(
                        book.getTitle(),
                        book.getStock()
                );
            }
        }

        for (CartItemResponse cartItem : cart.items()) {

            Book book = bookRepository.findById(cartItem.bookId())
                    .orElseThrow(() ->
                            new BookNotFoundException(cartItem.bookId()));

            book.reduceStock(cartItem.quantity());
        }

        Order order = OrderFactory.createOrder(cart);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart();

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus()
        );
    }
}