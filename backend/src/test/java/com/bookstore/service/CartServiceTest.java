package com.bookstore.service;

import com.bookstore.dto.cart.AddToCartRequest;
import com.bookstore.dto.cart.CartResponse;
import com.bookstore.entity.Book;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.CartItemNotFoundException;
import com.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void addBookToCart() {

        Book book = createBook(
                1L,
                "Clean Code",
                "Robert C. Martin",
                BigDecimal.valueOf(500)
        );

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        CartResponse response = cartService.addToCart(
                new AddToCartRequest(1L, 2)
        );

        assertNotNull(response);
        assertEquals(1, response.items().size());
        assertEquals(2, response.items().get(0).quantity());
        assertEquals(BigDecimal.valueOf(1000), response.total());
    }

    @Test
    void addingSameBookAgainShouldIncreaseQuantity() {

        Book book = createBook(
                1L,
                "Clean Code",
                "Robert C. Martin",
                BigDecimal.valueOf(500)
        );

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        cartService.addToCart(new AddToCartRequest(1L, 1));
        CartResponse response = cartService.addToCart(
                new AddToCartRequest(1L, 2)
        );

        assertEquals(1, response.items().size());
        assertEquals(3, response.items().get(0).quantity());
        assertEquals(BigDecimal.valueOf(1500), response.total());
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

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(cleanCode));
        when(bookRepository.findById(2L))
                .thenReturn(Optional.of(effectiveJava));
        cartService.addToCart(new AddToCartRequest(1L, 2));

        CartResponse response = cartService.addToCart(
                new AddToCartRequest(2L, 1)
        );
        assertEquals(2, response.items().size());
        assertEquals(BigDecimal.valueOf(1700), response.total());
    }

    @Test
    void updateCartItemQuantity() {

        Book book = createBook(
                1L,
                "Clean Code",
                "Robert C. Martin",
                BigDecimal.valueOf(500)
        );

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        cartService.addToCart(new AddToCartRequest(1L, 1));
        CartResponse response = cartService.updateQuantity(1L, 3);

        assertEquals(3, response.items().get(0).quantity());
        assertEquals(BigDecimal.valueOf(1500), response.total());
    }

    @Test
    void removeBookFromCart() {

        Book book = createBook(
                1L,
                "Effective Java",
                "Joshua Bloch",
                BigDecimal.valueOf(700)
        );

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        cartService.addToCart(new AddToCartRequest(1L, 1));
        CartResponse response = cartService.removeFromCart(1L);

        assertTrue(response.items().isEmpty());
        assertEquals(BigDecimal.ZERO, response.total());
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

        assertThrows(
                CartItemNotFoundException.class,
                () -> cartService.updateQuantity(99L, 2)
        );
    }

    private Book createBook(
            Long id,
            String title,
            String author,
            BigDecimal price) {

        Book book = new Book(title, author, price);
        ReflectionTestUtils.setField(book, "id", id);

        return book;
    }
}