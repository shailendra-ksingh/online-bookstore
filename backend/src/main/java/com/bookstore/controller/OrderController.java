package com.bookstore.controller;

import com.bookstore.dto.order.OrderResponse;
import com.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder() {

        log.info("Order creation request received");
        OrderResponse response = orderService.createOrder();
        log.info("Order created successfully. OrderId={}", response.orderId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}