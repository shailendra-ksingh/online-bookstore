package com.bookstore.service;

import com.bookstore.dto.book.BookResponse;

import java.util.List;

public interface BookService {

    List<BookResponse> getAllBooks();
}