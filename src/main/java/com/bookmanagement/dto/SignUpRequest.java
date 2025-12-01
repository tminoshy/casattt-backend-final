package com.bookmanagement.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
    private String email;
}

