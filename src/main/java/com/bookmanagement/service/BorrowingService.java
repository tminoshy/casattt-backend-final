package com.bookmanagement.service;

import com.bookmanagement.dto.BorrowRequest;
import com.bookmanagement.dto.InvoiceResponse;
import com.bookmanagement.entity.Book;
import com.bookmanagement.entity.Borrowing;
import com.bookmanagement.entity.Notification;
import com.bookmanagement.entity.User;
import com.bookmanagement.repository.BookRepository;
import com.bookmanagement.repository.BorrowingRepository;
import com.bookmanagement.repository.NotificationRepository;
import com.bookmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final NotificationRepository notificationRepository;

    public Borrowing borrowBook(Long userId, BorrowRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getStatus() != Book.BookStatus.AVAILABLE) {
            throw new RuntimeException("Book is not available");
        }

        if (book.getNumberOfBook() < request.getNumberOfBooks()) {
            throw new RuntimeException("Not enough books available");
        }

        Borrowing existingBorrowing = borrowingRepository
                .findByUserAndBookAndReturnDateIsNull(user, book)
                .orElse(null);

        if (existingBorrowing != null) {
            existingBorrowing.setNumberOfBooks(existingBorrowing.getNumberOfBooks() + request.getNumberOfBooks());
            book.setNumberOfBook(book.getNumberOfBook() - request.getNumberOfBooks());
            if (book.getNumberOfBook() == 0) {
                book.setStatus(Book.BookStatus.NOT_AVAILABLE);
            }
            bookRepository.save(book);

            Borrowing updated = borrowingRepository.save(existingBorrowing);

            Notification notification = Notification.builder()
                    .issueAt(LocalDateTime.now())
                    .notiticationStatus(Notification.NotificationStatus.SUCCESSFULLY_BORROWED)
                    .user(user)
                    .book(book)
                    .borowing(updated)
                    .message("Successfully borrowed " + book.getBookName())
                    .build();

            notificationRepository.save(notification);

            return updated;
        }

        // Create new borrowing
        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setBook(book);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setNumberOfBooks(request.getNumberOfBooks());
        borrowing.setStatus("Not Return Yet");

        book.setNumberOfBook(book.getNumberOfBook() - request.getNumberOfBooks());
        if (book.getNumberOfBook() == 0) {
            book.setStatus(Book.BookStatus.NOT_AVAILABLE);
        }
        bookRepository.save(book);

        // 👉 SAVE BORROWING FIRST
        Borrowing savedBorrowing = borrowingRepository.save(borrowing);

        // THEN CREATE NOTIFICATION
        Notification notification = Notification.builder()
                .issueAt(LocalDateTime.now())
                .notiticationStatus(Notification.NotificationStatus.SUCCESSFULLY_BORROWED)
                .user(user)
                .book(book)
                .borowing(savedBorrowing)
                .message("Successfully borrowed " + book.getBookName())
                .build();

        notificationRepository.save(notification);

        return savedBorrowing;
    }


    public Borrowing returnBook(Long userId, String bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(Long.parseLong(bookId))
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Borrowing borrowing = borrowingRepository
                .findByUserAndBookAndReturnDateIsNull(user, book)
                .orElseThrow(() -> new RuntimeException("No active borrowing found for this book"));

        borrowing.setReturnDate(LocalDate.now());
        
        // Calculate status
        long daysBetween = ChronoUnit.DAYS.between(borrowing.getBorrowDate(), borrowing.getReturnDate());
        if (daysBetween > 7) {
            borrowing.setStatus("Lated");
        } else {
            borrowing.setStatus("OK");
        }

        // Update book quantity
        book.setNumberOfBook(book.getNumberOfBook() + borrowing.getNumberOfBooks());
        if (book.getStatus() == Book.BookStatus.NOT_AVAILABLE && book.getNumberOfBook() > 0) {
            book.setStatus(Book.BookStatus.AVAILABLE);
        }
        bookRepository.save(book);

        Notification notification = Notification.builder()
                .issueAt(LocalDateTime.now())
                .notiticationStatus(Notification.NotificationStatus.SUCCESSFULLY_RETURNED)
                .user(user)
                .book(book)
                .borowing(borrowing)
                .message("Successfully returned " + book.getBookName())
                .build();

        notificationRepository.save(notification);

        return borrowingRepository.save(borrowing);
    }

    public List<Borrowing> getCurrentBorrowings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return borrowingRepository.findByUserAndReturnDateIsNull(user);
    }

    public List<Borrowing> getAllBorrowingHistory(Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (admin.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Only admins can view all borrowing history");
        }
        
        List<Borrowing> borrowings = borrowingRepository.findAllByOrderByBorrowDateDesc();
        // Update status for borrowings that haven't been returned
        for (Borrowing borrowing : borrowings) {
            if (borrowing.getReturnDate() == null) {
                long daysBetween = ChronoUnit.DAYS.between(borrowing.getBorrowDate(), LocalDate.now());
                if (daysBetween > 7) {
                    borrowing.setStatus("Lated");
                } else {
                    borrowing.setStatus("Not Return Yet");
                }
                borrowingRepository.save(borrowing);
            } else {
                long daysBetween = ChronoUnit.DAYS.between(borrowing.getBorrowDate(), borrowing.getReturnDate());
                if (daysBetween > 7) {
                    borrowing.setStatus("Lated");
                } else {
                    borrowing.setStatus("OK");
                }
                borrowingRepository.save(borrowing);
            }
        }
        return borrowings;
    }

    public List<InvoiceResponse> getAllInvoices(Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (admin.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Only admins can view all invoices");
        }
        
        List<Borrowing> borrowings = borrowingRepository.findAllByOrderByBorrowDateDesc();
        return borrowings.stream().map(borrowing -> {
            InvoiceResponse invoice = new InvoiceResponse();
            invoice.setBorrowerName(borrowing.getUser().getFirstName() + " " + borrowing.getUser().getLastName());
            invoice.setBookName(borrowing.getBook().getBookName());
            invoice.setBookImageUrl(borrowing.getBook().getImageUrl());
            invoice.setBookPrice(borrowing.getBook().getPrice());
            invoice.setNumberOfBooks(borrowing.getNumberOfBooks());
            invoice.setBorrowDate(borrowing.getBorrowDate().toString());
            invoice.setReturnDate(borrowing.getReturnDate() != null ? borrowing.getReturnDate().toString() : "Not returned");
            invoice.setStatus(borrowing.getStatus());
            return invoice;
        }).collect(Collectors.toList());
    }

    public Borrowing updateBorrowingQuantity(Long userId, String bookId, Integer newQuantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(Long.parseLong(bookId))
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Borrowing borrowing = borrowingRepository
                .findByUserAndBookAndReturnDateIsNull(user, book)
                .orElseThrow(() -> new RuntimeException("No active borrowing found for this book"));

        int quantityDifference = newQuantity - borrowing.getNumberOfBooks();
        
        if (quantityDifference > 0) {
            // Increasing quantity
            if (book.getNumberOfBook() < quantityDifference) {
                throw new RuntimeException("Not enough books available");
            }
            book.setNumberOfBook(book.getNumberOfBook() - quantityDifference);
        } else {
            // Decreasing quantity
            book.setNumberOfBook(book.getNumberOfBook() + Math.abs(quantityDifference));
            if (book.getStatus() == Book.BookStatus.NOT_AVAILABLE && book.getNumberOfBook() > 0) {
                book.setStatus(Book.BookStatus.AVAILABLE);
            }
        }

        borrowing.setNumberOfBooks(newQuantity);
        bookRepository.save(book);
        return borrowingRepository.save(borrowing);
    }
}

