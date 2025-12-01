package com.bookmanagement.controller;

import com.bookmanagement.dto.BorrowRequest;
import com.bookmanagement.dto.InvoiceResponse;
import com.bookmanagement.entity.Borrowing;
import com.bookmanagement.service.BorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/borrowing")
@CrossOrigin(origins = "*")
public class BorrowingController {

    private final BorrowingService borrowingService;

    @PostMapping("/borrow/{userId}")
    public ResponseEntity<?> borrowBook(@PathVariable Long userId, @RequestBody BorrowRequest request) {
        try {
            Borrowing borrowing = borrowingService.borrowBook(userId, request);
            return ResponseEntity.ok(borrowing);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/return/{userId}/{bookId}")
    public ResponseEntity<?> returnBook(@PathVariable Long userId, @PathVariable String bookId) {
        try {
            Borrowing borrowing = borrowingService.returnBook(userId, bookId);
            return ResponseEntity.ok(borrowing);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/current/{userId}")
    public ResponseEntity<List<Borrowing>> getCurrentBorrowings(@PathVariable Long userId) {
        List<Borrowing> borrowings = borrowingService.getCurrentBorrowings(userId);
        return ResponseEntity.ok(borrowings);
    }

    @PutMapping("/update/{userId}/{bookId}")
    public ResponseEntity<?> updateBorrowingQuantity(
            @PathVariable Long userId,
            @PathVariable String bookId,
            @RequestParam Integer quantity) {
        try {
            Borrowing borrowing = borrowingService.updateBorrowingQuantity(userId, bookId, quantity);
            return ResponseEntity.ok(borrowing);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/history/{adminId}")
    public ResponseEntity<?> getAllBorrowingHistory(@PathVariable Long adminId) {
        try {
            List<Borrowing> borrowings = borrowingService.getAllBorrowingHistory(adminId);
            return ResponseEntity.ok(borrowings);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/invoices/{adminId}")
    public ResponseEntity<?> getAllInvoices(@PathVariable Long adminId) {
        try {
            List<InvoiceResponse> invoices = borrowingService.getAllInvoices(adminId);
            return ResponseEntity.ok(invoices);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

