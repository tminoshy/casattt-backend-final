package com.bookmanagement.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserInformationResponse {
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
    private String sex;
    private LocalDate dOb;
}
