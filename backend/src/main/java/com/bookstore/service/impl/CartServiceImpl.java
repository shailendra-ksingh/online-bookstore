package com.bookstore.service.impl;

import com.bookstore.dto.cart.AddToCartRequest;
import com.bookstore.dto.cart.CartItemResponse;
import com.bookstore.dto.cart.CartResponse;
import com.bookstore.entity.Book;
import com.bookstore.entity.Cart;
import com.bookstore.entity.CartItem;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.CartItemNotFoundException;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.repository.CartRepository;
import com.bookstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final BookRepository bookRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartResponse addToCart(AddToCartRequest request) {
        if (request.quantity() <= 0) {
            throw new IllegalStateException(
                    "Quantity must be greater than zero"
            );
        }

        Book book = findBook(request.bookId());
        Cart cart = getOrCreateCart();

        CartItem cartItem = cartItemRepository
                .findByCartIdAndBookId(
                        cart.getId(),
                        book.getId()
                )
                .orElseGet(() ->
                        CartItem.builder()
                                .cart(cart)
                                .book(book)
                                .quantity(0)
                                .build()
                );

        cartItem.setQuantity(
                cartItem.getQuantity() + request.quantity()
        );

        cartItemRepository.save(cartItem);

        return getCart();
    }

    @Override
    public CartResponse getCart() {
        Cart cart = getOrCreateCart();

        List<CartItemResponse> items =
                cartItemRepository.findAllWithBooks(cart.getId())
                        .stream()
                        .map(this::toCartItemResponse)
                        .toList();

        BigDecimal total = items.stream()
                .map(CartItemResponse::itemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(items, total);
    }

    @Override
    public CartResponse updateQuantity(
            Long bookId,
            Integer quantity) {

        CartItem cartItem = findCartItem(bookId);

        if (quantity <= 0) {
            return removeFromCart(bookId);
        }

        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);

        return getCart();
    }

    @Override
    public CartResponse removeFromCart(Long bookId) {
        CartItem cartItem = findCartItem(bookId);

        cartItemRepository.delete(cartItem);

        return getCart();
    }

    @Override
    public void clearCart() {
        Cart cart = getOrCreateCart();

        cartItemRepository.deleteByCartId(cart.getId());
    }

    private CartItem findCartItem(Long bookId) {
        Cart cart = getOrCreateCart();

        return cartItemRepository
                .findByCartIdAndBookId(
                        cart.getId(),
                        bookId
                )
                .orElseThrow(() ->
                        new CartItemNotFoundException(bookId)
                );
    }

    private Cart getOrCreateCart() {
        return cartRepository.findFirstByOrderByIdAsc()
                .orElseGet(() ->
                        cartRepository.save(
                                Cart.builder()
                                        .build()
                        )
                );
    }

    private Book findBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new BookNotFoundException(bookId)
                );
    }

    private CartItemResponse toCartItemResponse(CartItem cartItem) {
        Book book = cartItem.getBook();

        BigDecimal itemTotal = book.getPrice()
                .multiply(
                        BigDecimal.valueOf(
                                cartItem.getQuantity()
                        )
                );

        return new CartItemResponse(
                book.getId(),
                book.getTitle(),
                book.getPrice(),
                cartItem.getQuantity(),
                itemTotal
        );
    }
}
