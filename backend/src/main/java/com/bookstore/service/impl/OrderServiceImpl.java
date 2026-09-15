package com.bookstore.service.impl;

import com.bookstore.dto.payment.PaymentMethod;
import com.bookstore.dto.payment.PaymentResult;
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
import com.bookstore.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public OrderResponse createOrder() {

        CartResponse cart = cartService.getCart();

        if (cart.items().isEmpty()) {
            log.warn("Checkout requested with an empty cart");

            throw new IllegalStateException(
                    "Cannot create order because cart is empty"
            );
        }

        log.info(
                "Starting checkout for {} item(s)",
                cart.items().size()
        );

        Map<Long, Book> books = new HashMap<>();

        // 1. Validate stock for all items
        for (CartItemResponse item : cart.items()) {

            Book book = bookRepository.findById(item.bookId())
                    .orElseThrow(() ->
                            new BookNotFoundException(item.bookId()));

            if (!book.hasEnoughStock(item.quantity())) {

                log.warn(
                        "Insufficient stock for book {}, requested={}, available={}",
                        item.bookId(),
                        item.quantity(),
                        book.getStock()
                );

                throw new InsufficientStockException(
                        book.getTitle(),
                        book.getStock()
                );
            }

            books.put(book.getId(), book);
        }

        // 2. Process payment
        PaymentResult paymentResult =
                paymentService.processPayment(
                        cart.total(),
                        PaymentMethod.CARD
                );

        if (!paymentResult.successful()) {

            log.warn("Payment failed during checkout");

            throw new IllegalStateException(
                    "Payment failed"
            );
        }

        // 3. Create order
//        Order order = Order.fromCart(cart);
        Order order = OrderFactory.createOrder(cart);

        // 4. Reduce stock
        for (CartItemResponse item : cart.items()) {
            books.get(item.bookId())
                    .reduceStock(item.quantity());
        }

        // 5. Save order
        Order savedOrder =
                orderRepository.save(order);

        // 6. Clear cart
        cartService.clearCart();

        log.info(
                "Checkout completed successfully. orderId={}, amount={}",
                savedOrder.getId(),
                savedOrder.getTotalAmount()
        );

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus()
        );
    }
}