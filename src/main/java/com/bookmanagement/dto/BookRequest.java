package com.bookmanagement.dto;

import lombok.Data;

@Data
public class BookRequest {
    private String bookName;
    private String author;
    private String degree;
    private Double price;
    private String imageUrl;
    private String description;
    private Integer numberOfBook;
    private String genre;
}

