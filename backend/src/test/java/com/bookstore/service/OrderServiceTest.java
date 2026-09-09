package com.bookstore.service;

import com.bookstore.dto.cart.CartItemResponse;
import com.bookstore.dto.cart.CartResponse;
import com.bookstore.dto.order.OrderResponse;
import com.bookstore.entity.Order;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CartService cartService;

    @Mock
    private OrderRepository orderRepository;

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

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order order = invocation.getArgument(0);
                    order.setId(1L);
                    return order;
                });

        OrderResponse response = orderService.createOrder();

        assertNotNull(response);
        assertEquals(1L, response.orderId());

        assertEquals(
                new BigDecimal("1000.00"),
                response.total()
        );

        assertEquals("CONFIRMED", response.status());

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

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order order = invocation.getArgument(0);
                    order.setId(2L);
                    return order;
                });

        orderService.createOrder();

        ArgumentCaptor<Order> captor =
                ArgumentCaptor.forClass(Order.class);

        verify(orderRepository).save(captor.capture());

        Order savedOrder = captor.getValue();

        assertEquals(
                new BigDecimal("2250.00"),
                savedOrder.getTotalAmount()
        );

        assertEquals(1, savedOrder.getItems().size());

        assertEquals(
                2L,
                savedOrder.getItems().get(0).getBookId()
        );

        assertEquals(
                "Effective Java",
                savedOrder.getItems().get(0).getTitle()
        );

        assertEquals(
                3,
                savedOrder.getItems().get(0).getQuantity()
        );

        assertEquals(
                new BigDecimal("2250.00"),
                savedOrder.getItems().get(0).getItemTotal()
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
}