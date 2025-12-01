package com.bookmanagement.service;

import com.bookmanagement.dto.BookRequest;
import com.bookmanagement.dto.BookResponseAdmin;
import com.bookmanagement.dto.BookResponseById;
import com.bookmanagement.dto.BookResponseUser;
import com.bookmanagement.entity.Book;
import com.bookmanagement.entity.User;
import com.bookmanagement.repository.BookRepository;
import com.bookmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public Book addBook(Long adminId, BookRequest request) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (admin.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Only admins can add books");
        }

        Book book = new Book();
        book.setBookName(request.getBookName());
        book.setDegree(request.getDegree());
        book.setPrice(request.getPrice());
        book.setImageUrl(request.getImageUrl());
        book.setDescription(request.getDescription());
        book.setNumberOfBook(request.getNumberOfBook());
        book.setGenre(request.getGenre());
        book.setAuthor(request.getAuthor());
        book.setStatus(Book.BookStatus.AVAILABLE);

        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<BookResponseUser> filterBooks(String priceRange, String genre) {
        List<Book> books;

        if (genre != null && !genre.isEmpty()) {
            if (priceRange != null && !priceRange.isEmpty()) {
                Double[] priceBounds = getPriceBounds(priceRange);
                books = bookRepository.findByGenreAndPriceRange(genre, priceBounds[0], priceBounds[1]);
            } else {
                books = bookRepository.findByGenre(genre);
            }
        } else {
            if (priceRange != null && !priceRange.isEmpty()) {
                Double[] priceBounds = getPriceBounds(priceRange);
                books = bookRepository.findByPriceRange(priceBounds[0], priceBounds[1]);
            } else {
                books = bookRepository.findAll();
            }
        }

        return books
                .stream()
                .map(book -> {
                    BookResponseUser response = new BookResponseUser();
                    response.setBookName(book.getBookName());
                    response.setPrice(book.getPrice());
                    response.setImageUrl(book.getImageUrl());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<BookResponseAdmin> getAllBooksAdmin() {
        return bookRepository.findAll()
                .stream()
                .map(book -> {
                    BookResponseAdmin response = new BookResponseAdmin();
                    response.setBookId(book.getBookId());
                    response.setBookName(book.getBookName());
                    response.setPrice(book.getPrice());
                    response.setImageUrl(book.getImageUrl());
                    return response;
                })
                .collect(Collectors.toList());
    }

    private Double[] getPriceBounds(String priceRange) {
        switch (priceRange) {
            case "0-50000":
                return new Double[]{0.0, 50000.0};
            case "50000-100000":
                return new Double[]{50000.0, 100000.0};
            case "100000-200000":
                return new Double[]{100000.0, 200000.0};
            case "200000+":
                return new Double[]{200000.0, Double.MAX_VALUE};
            default:
                return new Double[]{0.0, Double.MAX_VALUE};
        }
    }

    public Book getBookById(String bookId) {
        return bookRepository.findById(Long.parseLong(bookId))
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public BookResponseById getBookByIdUser(String bookId) {
        return bookRepository.findById(Long.parseLong(bookId))
                .map(book -> {
                    BookResponseById response = new BookResponseById();
                    response.setBookName(book.getBookName());
                    response.setPrice(book.getPrice());
                    response.setStatus(book.getStatus());
                    response.setDegree(book.getDegree());
                    response.setDescription(book.getDescription());
                    response.setImageUrl(book.getImageUrl());
                    response.setAuthor(book.getAuthor());
                    return response;
                })
                .orElse(null);
    }
}

