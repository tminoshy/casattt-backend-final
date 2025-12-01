package com.bookmanagement.dto;

import lombok.Data;

@Data
public class BookResponseUser {
    private String bookName;
    private double price;
    private String imageUrl;
}
