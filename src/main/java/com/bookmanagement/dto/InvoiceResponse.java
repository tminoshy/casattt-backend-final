package com.bookmanagement.dto;

import lombok.Data;

@Data
public class InvoiceResponse {
    private String borrowerName;
    private String bookName;
    private String bookImageUrl;
    private Double bookPrice;
    private Integer numberOfBooks;
    private String borrowDate;
    private String returnDate;
    private String status;
}

