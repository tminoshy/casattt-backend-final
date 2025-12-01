package com.bookmanagement.dto;

import com.bookmanagement.entity.Book;
import lombok.Data;

@Data
public class BookResponseById {
    private String imageUrl;
    private String degree;
    private Book.BookStatus status;
    private String author;
    private String description;
    private double price;
    private String bookName;
}
