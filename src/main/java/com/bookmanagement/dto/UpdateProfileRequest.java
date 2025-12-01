package com.bookmanagement.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
}

