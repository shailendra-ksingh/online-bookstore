package com.bookstore.service;

import com.bookstore.dto.cart.CartItemResponse;
import com.bookstore.dto.cart.CartResponse;
import com.bookstore.dto.order.OrderResponse;
import com.bookstore.entity.Book;
import com.bookstore.entity.Order;
import com.bookstore.entity.OrderStatus;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.OrderRepository;
import com.bookstore.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CartService cartService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BookRepository bookRepository;


    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldCreateOrderFromCart() {

        CartItemResponse item = new CartItemResponse(
                1L,
                "Clean Code",
                new BigDecimal("500.00"),
                2,
                new BigDecimal("1000.00")
        );

        CartResponse cartResponse = new CartResponse(
                List.of(item),
                new BigDecimal("1000.00")
        );

        when(cartService.getCart()).thenReturn(cartResponse);
        Book book = new Book(
                "Clean Code",
                "Robert C. Martin",
                new BigDecimal("500.00"),
                10
        );

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));
        Order savedOrder = Order.builder()
                .id(1L)
                .totalAmount(new BigDecimal("1000.00"))
                .build();

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        OrderResponse response = orderService.createOrder();

        assertNotNull(response);
        assertEquals(1L, response.orderId());

        assertEquals(
                new BigDecimal("1000.00"),
                response.total()
        );

        assertEquals(OrderStatus.CONFIRMED, response.status());

        verify(orderRepository).save(any(Order.class));
        verify(cartService).clearCart();
    }

    @Test
    void shouldCreateOrderWithCorrectItems() {

        CartItemResponse item = new CartItemResponse(
                2L,
                "Effective Java",
                new BigDecimal("750.00"),
                3,
                new BigDecimal("2250.00")
        );

        CartResponse cartResponse = new CartResponse(
                List.of(item),
                new BigDecimal("2250.00")
        );

        when(cartService.getCart()).thenReturn(cartResponse);
        Book book = new Book(
                "Effective Java",
                "Joshua Bloch",
                new BigDecimal("750.00"),
                10
        );

        when(bookRepository.findById(2L))
                .thenReturn(Optional.of(book));
        Order savedOrder = Order.builder()
                .id(2L)
                .totalAmount(new BigDecimal("1000.00"))
                .build();

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        orderService.createOrder();

        ArgumentCaptor<Order> captor =
                ArgumentCaptor.forClass(Order.class);

        verify(orderRepository).save(captor.capture());

        Order capturedOrder = captor.getValue();

        assertEquals(
                new BigDecimal("2250.00"),
                capturedOrder.getTotalAmount()
        );

        assertEquals(1, capturedOrder.getItems().size());

        assertEquals(
                2L,
                capturedOrder.getItems().get(0).getBookId()
        );

        assertEquals(
                "Effective Java",
                capturedOrder.getItems().get(0).getTitle()
        );

        assertEquals(
                3,
                capturedOrder.getItems().get(0).getQuantity()
        );

        assertEquals(
                new BigDecimal("2250.00"),
                capturedOrder.getItems().get(0).getItemTotal()
        );

        verify(cartService).clearCart();
    }

    @Test
    void shouldNotCreateOrderWhenCartIsEmpty() {

        CartResponse emptyCart = new CartResponse(
                List.of(),
                BigDecimal.ZERO
        );

        when(cartService.getCart()).thenReturn(emptyCart);

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> orderService.createOrder()
                );

        assertEquals(
                "Cannot create order because cart is empty",
                exception.getMessage()
        );

        verify(orderRepository, never()).save(any(Order.class));
        verify(cartService, never()).clearCart();
    }

    @Test
    void shouldNotCreateOrderWhenStockIsInsufficient() {

        CartItemResponse item = new CartItemResponse(
                1L,
                "Clean Code",
                new BigDecimal("500.00"),
                5,
                new BigDecimal("2500.00")
        );

        CartResponse cartResponse = new CartResponse(
                List.of(item),
                new BigDecimal("2500.00")
        );

        Book book = new Book(
                "Clean Code",
                "Robert C. Martin",
                new BigDecimal("500.00"),
                2
        );

        when(cartService.getCart()).thenReturn(cartResponse);

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        assertThrows(
                IllegalStateException.class,
                () -> orderService.createOrder()
        );

        verify(orderRepository, never()).save(any(Order.class));
        verify(cartService, never()).clearCart();
    }

}