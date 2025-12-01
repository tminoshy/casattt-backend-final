package com.bookmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @Column(name = "book_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookId;

    @Column(name = "book_name", nullable = false)
    private String bookName;

    private String author;

    @Column(nullable = false)
    private String degree; // 100% new, 99% like new, 95%, 90%, 80%

    @Column(nullable = false)
    private Double price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "number_of_book", nullable = false)
    private Integer numberOfBook;

    @Column(nullable = false)
    private String genre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

    public enum BookStatus {
        AVAILABLE, NOT_AVAILABLE
    }
}

