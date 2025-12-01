package com.bookmanagement.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class BookResponseAdmin {

    private long bookId;
    private String bookName;
    private String imageUrl;
    private Double price;
}
