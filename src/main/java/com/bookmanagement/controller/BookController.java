package com.bookmanagement.controller;

import com.bookmanagement.dto.BookRequest;
import com.bookmanagement.dto.BookResponseAdmin;
import com.bookmanagement.dto.BookResponseById;
import com.bookmanagement.dto.BookResponseUser;
import com.bookmanagement.entity.Book;
import com.bookmanagement.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/{adminId}")
    public ResponseEntity<?> addBook(@PathVariable Long adminId, @RequestBody BookRequest request) {
        try {
            Book book = bookService.addBook(adminId, request);
            return ResponseEntity.ok(book);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<BookResponseUser>> getAllBooks(
            @RequestParam(required = false) String priceRange,
            @RequestParam(required = false) String genre) {
        List<BookResponseUser> books = bookService.filterBooks(priceRange, genre);
        return ResponseEntity.ok(books);
    }

    @GetMapping("user/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable String bookId) {
        try {
            BookResponseById book = bookService.getBookByIdUser(bookId);
            return ResponseEntity.ok(book);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getAllBookAdmin() {
        try {
            List<BookResponseAdmin> lists = bookService.getAllBooksAdmin();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(lists);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}

