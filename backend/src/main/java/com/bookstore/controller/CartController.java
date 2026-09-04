package com.bookstore.controller;

import com.bookstore.dto.cart.AddToCartRequest;
import com.bookstore.dto.cart.CartResponse;
import com.bookstore.dto.cart.UpdateCartQuantityRequest;
import com.bookstore.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> addToCart(
            @Valid @RequestBody AddToCartRequest request) {

        log.info("Adding book to cart");
        return ResponseEntity.ok(cartService.addToCart(request));
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {

        log.info("Fetching shopping cart");
        return ResponseEntity.ok(cartService.getCart());
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<CartResponse> updateQuantity(
            @PathVariable @Positive(message = "Book ID must be greater than zero")
            Long bookId,
            @Valid @RequestBody UpdateCartQuantityRequest request) {

        log.info("Updating cart item quantity");
        return ResponseEntity.ok(
                cartService.updateQuantity(bookId, request.quantity())
        );
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<CartResponse> removeFromCart(
            @PathVariable @Positive(message = "Book ID must be greater than zero")
            Long bookId) {

        log.info("Removing book from cart");
        return ResponseEntity.ok(cartService.removeFromCart(bookId));
    }
}