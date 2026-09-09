package com.bookstore.service;

import com.bookstore.dto.cart.AddToCartRequest;
import com.bookstore.dto.cart.CartResponse;
import com.bookstore.entity.Book;
import com.bookstore.entity.Cart;
import com.bookstore.entity.CartItem;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.CartItemNotFoundException;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.repository.CartRepository;
import com.bookstore.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void addBookToCart() {

        Book book = createBook(
                1L,
                "Clean Code",
                "Robert C. Martin",
                BigDecimal.valueOf(500)
        );

        Cart cart = createCart(1L);
        List<CartItem> savedItems = new ArrayList<>();

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(cartRepository.findFirstByOrderByIdAsc())
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartIdAndBookId(1L, 1L))
                .thenReturn(Optional.empty());

        when(cartItemRepository.save(any(CartItem.class)))
                .thenAnswer(invocation -> {
                    CartItem item = invocation.getArgument(0);
                    savedItems.add(item);
                    return item;
                });

        when(cartItemRepository.findAllWithBooks(1L))
                .thenAnswer(invocation -> savedItems);

        CartResponse response = cartService.addToCart(
                new AddToCartRequest(1L, 2)
        );

        assertNotNull(response);
        assertEquals(1, response.items().size());
        assertEquals(2, response.items().get(0).quantity());

        assertEquals(
                0,
                response.total().compareTo(BigDecimal.valueOf(1000))
        );
    }

    @Test
    void addingSameBookAgainShouldIncreaseQuantity() {

        Book book = createBook(
                1L,
                "Clean Code",
                "Robert C. Martin",
                BigDecimal.valueOf(500)
        );

        Cart cart = createCart(1L);

        CartItem existingItem = createCartItem(
                1L,
                cart,
                book,
                1
        );

        List<CartItem> savedItems = new ArrayList<>();
        savedItems.add(existingItem);

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        when(cartRepository.findFirstByOrderByIdAsc())
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartIdAndBookId(1L, 1L))
                .thenReturn(Optional.of(existingItem));

        when(cartItemRepository.save(any(CartItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(cartItemRepository.findAllWithBooks(1L))
                .thenReturn(savedItems);

        CartResponse response = cartService.addToCart(
                new AddToCartRequest(1L, 2)
        );

        assertEquals(1, response.items().size());
        assertEquals(3, response.items().get(0).quantity());

        assertEquals(
                0,
                response.total().compareTo(BigDecimal.valueOf(1500))
        );
    }

    @Test
    void calculateTotalForMultipleBooks() {

        Book cleanCode = createBook(
                1L,
                "Clean Code",
                "Robert C. Martin",
                BigDecimal.valueOf(500)
        );

        Book effectiveJava = createBook(
                2L,
                "Effective Java",
                "Joshua Bloch",
                BigDecimal.valueOf(700)
        );

        Cart cart = createCart(1L);
        List<CartItem> savedItems = new ArrayList<>();

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(cleanCode));

        when(bookRepository.findById(2L))
                .thenReturn(Optional.of(effectiveJava));

        when(cartRepository.findFirstByOrderByIdAsc())
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartIdAndBookId(
                eq(1L),
                any(Long.class)))
                .thenAnswer(invocation -> {

                    Long bookId = invocation.getArgument(1);

                    return savedItems.stream()
                            .filter(item ->
                                    item.getBook()
                                            .getId()
                                            .equals(bookId))
                            .findFirst();
                });

        when(cartItemRepository.save(any(CartItem.class)))
                .thenAnswer(invocation -> {

                    CartItem item = invocation.getArgument(0);

                    if (!savedItems.contains(item)) {
                        savedItems.add(item);
                    }

                    return item;
                });

        when(cartItemRepository.findAllWithBooks(1L))
                .thenAnswer(invocation -> savedItems);

        cartService.addToCart(
                new AddToCartRequest(1L, 2)
        );

        CartResponse response = cartService.addToCart(
                new AddToCartRequest(2L, 1)
        );

        assertEquals(2, response.items().size());

        assertEquals(
                0,
                response.total().compareTo(BigDecimal.valueOf(1700))
        );
    }

    @Test
    void updateCartItemQuantity() {

        Book book = createBook(
                1L,
                "Clean Code",
                "Robert C. Martin",
                BigDecimal.valueOf(500)
        );

        Cart cart = createCart(1L);

        CartItem cartItem = createCartItem(
                1L,
                cart,
                book,
                1
        );

        when(cartRepository.findFirstByOrderByIdAsc())
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartIdAndBookId(1L, 1L))
                .thenReturn(Optional.of(cartItem));

        when(cartItemRepository.save(any(CartItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(cartItemRepository.findAllWithBooks(1L))
                .thenReturn(List.of(cartItem));

        CartResponse response = cartService.updateQuantity(
                1L,
                3
        );

        assertEquals(3, response.items().get(0).quantity());

        assertEquals(
                0,
                response.total().compareTo(BigDecimal.valueOf(1500))
        );
    }

    @Test
    void removeBookFromCart() {

        Book book = createBook(
                1L,
                "Effective Java",
                "Joshua Bloch",
                BigDecimal.valueOf(700)
        );

        Cart cart = createCart(1L);

        CartItem cartItem = createCartItem(
                1L,
                cart,
                book,
                1
        );

        when(cartRepository.findFirstByOrderByIdAsc())
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartIdAndBookId(1L, 1L))
                .thenReturn(Optional.of(cartItem));

        when(cartItemRepository.findAllWithBooks(1L))
                .thenReturn(List.of());

        CartResponse response = cartService.removeFromCart(1L);

        assertTrue(response.items().isEmpty());

        assertEquals(
                0,
                response.total().compareTo(BigDecimal.ZERO)
        );

        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    void throwExceptionWhenBookDoesNotExist() {

        when(bookRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> cartService.addToCart(
                        new AddToCartRequest(99L, 1)
                )
        );
    }

    @Test
    void throwExceptionWhenUpdatingItemNotInCart() {

        Cart cart = createCart(1L);

        when(cartRepository.findFirstByOrderByIdAsc())
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartIdAndBookId(1L, 99L))
                .thenReturn(Optional.empty());

        assertThrows(
                CartItemNotFoundException.class,
                () -> cartService.updateQuantity(99L, 2)
        );
    }

    private Cart createCart(Long id) {

        Cart cart = new Cart();

        ReflectionTestUtils.setField(
                cart,
                "id",
                id
        );

        return cart;
    }

    private Book createBook(
            Long id,
            String title,
            String author,
            BigDecimal price) {

        Book book = new Book(
                title,
                author,
                price
        );

        ReflectionTestUtils.setField(
                book,
                "id",
                id
        );

        return book;
    }

    private CartItem createCartItem(
            Long id,
            Cart cart,
            Book book,
            Integer quantity) {

        CartItem cartItem = new CartItem();

        ReflectionTestUtils.setField(
                cartItem,
                "id",
                id
        );

        ReflectionTestUtils.setField(
                cartItem,
                "cart",
                cart
        );

        ReflectionTestUtils.setField(
                cartItem,
                "book",
                book
        );

        ReflectionTestUtils.setField(
                cartItem,
                "quantity",
                quantity
        );

        return cartItem;
    }
}