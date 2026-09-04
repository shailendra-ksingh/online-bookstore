package com.bookstore.service;

import com.bookstore.dto.cart.AddToCartRequest;
import com.bookstore.dto.cart.CartItemResponse;
import com.bookstore.dto.cart.CartResponse;
import com.bookstore.entity.Book;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.CartItemNotFoundException;
import com.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CartService {

    private final BookRepository bookRepository;

    // In-memory cart used for the assignment.
    // A production application need to persist cart data per user.
    private final Map<Long, Integer> cartItems = new ConcurrentHashMap<>();

    public CartResponse addToCart(AddToCartRequest request) {

        Book book = findBook(request.bookId());

        cartItems.merge(
                book.getId(),
                request.quantity(),
                Integer::sum
        );

        return getCart();
    }

    public CartResponse getCart() {

        List<CartItemResponse> items = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {

            Book book = findBook(entry.getKey());
            Integer quantity = entry.getValue();

            BigDecimal itemTotal = book.getPrice()
                    .multiply(BigDecimal.valueOf(quantity));

            items.add(new CartItemResponse(
                    book.getId(),
                    book.getTitle(),
                    book.getPrice(),
                    quantity,
                    itemTotal
            ));
        }

        BigDecimal total = items.stream()
                .map(CartItemResponse::itemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(items, total);
    }

    public CartResponse updateQuantity(Long bookId, Integer quantity) {

        validateCartItem(bookId);
        cartItems.put(bookId, quantity);

        return getCart();
    }

    public CartResponse removeFromCart(Long bookId) {

        validateCartItem(bookId);
        cartItems.remove(bookId);

        return getCart();
    }

    private void validateCartItem(Long bookId) {
        if (!cartItems.containsKey(bookId)) {
            throw new CartItemNotFoundException(bookId);
        }
    }

    public void clearCart() {
        cartItems.clear();
    }

    private Book findBook(Long bookId) {

        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }
}