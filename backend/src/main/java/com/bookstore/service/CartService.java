package com.bookstore.service;

import com.bookstore.dto.cart.AddToCartRequest;
import com.bookstore.dto.cart.CartItemResponse;
import com.bookstore.dto.cart.CartResponse;
import com.bookstore.entity.Book;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.CartItemNotFoundException;
import com.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartService {

    private final BookRepository bookRepository;

    /*
     * For this assignment, the cart is kept in memory to keep the implementation simple.
     * In a production application, cart data would typically be persisted and associated
     * with the authenticated user.
     */
    private final Map<Long, Integer> cartItems = new ConcurrentHashMap<>();

    public CartService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

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

        if (!cartItems.containsKey(bookId)) {
            throw new CartItemNotFoundException(bookId);
        }

        cartItems.put(bookId, quantity);

        return getCart();
    }

    public CartResponse removeFromCart(Long bookId) {

        if (!cartItems.containsKey(bookId)) {
            throw new CartItemNotFoundException(bookId);
        }

        cartItems.remove(bookId);

        return getCart();
    }

    private Book findBook(Long bookId) {

        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }
}