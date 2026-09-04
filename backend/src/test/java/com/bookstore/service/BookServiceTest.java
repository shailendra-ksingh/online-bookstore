package com.bookstore.service;

import com.bookstore.dto.book.BookResponse;
import com.bookstore.entity.Book;
import com.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;


    @Test
    void getAllBooks_shouldReturnBooks() {

        Book book1 = new Book(
                "Clean Code",
                "Robert C. Martin",
                BigDecimal.valueOf(500)
        );

        Book book2 = new Book(
                "Effective Java",
                "Joshua Bloch",
                BigDecimal.valueOf(700)
        );

        when(bookRepository.findAll())
                .thenReturn(List.of(book1, book2));

        List<BookResponse> books = bookService.getAllBooks();

        assertNotNull(books);
        assertEquals(2, books.size());

        assertEquals("Clean Code", books.get(0).title());
        assertEquals("Effective Java", books.get(1).title());
    }


    @Test
    void getAllBooks_shouldReturnEmptyList() {

        when(bookRepository.findAll()).thenReturn(List.of());
        List<BookResponse> books = bookService.getAllBooks();

        assertNotNull(books);
        assertTrue(books.isEmpty());
    }


    @Test
    void getAllBooks_shouldMapBookToResponse() {

        Book book = new Book(
                "Clean Architecture",
                "Robert C. Martin",
                BigDecimal.valueOf(650)
        );

        ReflectionTestUtils.setField(book, "id", 1L);
        when(bookRepository.findAll()).thenReturn(List.of(book));
        List<BookResponse> books = bookService.getAllBooks();

        assertEquals(1, books.size());
        BookResponse response = books.get(0);

        assertEquals(1L, response.id());
        assertEquals("Clean Architecture", response.title());
        assertEquals("Robert C. Martin", response.author());
        assertEquals(BigDecimal.valueOf(650), response.price());
    }
}