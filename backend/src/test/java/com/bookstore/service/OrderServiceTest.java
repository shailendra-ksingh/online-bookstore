package com.bookstore.service;

import com.bookstore.dto.cart.CartItemResponse;
import com.bookstore.dto.cart.CartResponse;
import com.bookstore.dto.order.OrderResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderService orderService;


    @Test
    void shouldCreateOrderSuccessfully() {

        CartItemResponse cartItem = new CartItemResponse(
                1L,
                "Clean Code",
                new BigDecimal("500.00"),
                2,
                new BigDecimal("1000.00")
        );

        CartResponse cart = new CartResponse(
                List.of(cartItem),
                new BigDecimal("1000.00")
        );
        when(cartService.getCart()).thenReturn(cart);
        OrderResponse order = orderService.createOrder();
        assertNotNull(order);
        assertEquals("CONFIRMED", order.status());
        assertEquals(new BigDecimal("1000.00"), order.total());

        verify(cartService).clearCart();
    }


    @Test
    void shouldNotCreateOrderForEmptyCart() {

        CartResponse emptyCart = new CartResponse(
                Collections.emptyList(),
                BigDecimal.ZERO
        );

        when(cartService.getCart()).thenReturn(emptyCart);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> orderService.createOrder()
        );
        assertEquals(
                "Cannot create order because cart is empty",
                exception.getMessage()
        );
    }


    @Test
    void shouldGenerateDifferentOrderIds() {

        CartItemResponse cartItem = new CartItemResponse(
                1L,
                "Spring Boot In Action",
                new BigDecimal("700.00"),
                1,
                new BigDecimal("700.00")
        );

        CartResponse cart = new CartResponse(
                List.of(cartItem),
                new BigDecimal("700.00")
        );

        when(cartService.getCart()).thenReturn(cart);
        OrderResponse firstOrder = orderService.createOrder();
        OrderResponse secondOrder = orderService.createOrder();

        assertNotEquals(
                firstOrder.orderId(),
                secondOrder.orderId()
        );
    }
}