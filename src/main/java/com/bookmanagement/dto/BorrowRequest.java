package com.bookmanagement.dto;

import lombok.Data;

@Data
public class BorrowRequest {
    private Long bookId;
    private Integer numberOfBooks;
}

