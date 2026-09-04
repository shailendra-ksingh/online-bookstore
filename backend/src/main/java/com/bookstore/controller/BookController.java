package com.bookstore.controller;

import com.bookstore.dto.book.BookResponse;
import com.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookResponse>> getBooks() {

        log.info("Fetching available books");
        List<BookResponse> books = bookService.getAllBooks();
        log.info("Retrieved {} books", books.size());
        return ResponseEntity.ok(books);
    }
}