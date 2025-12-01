package com.bookmanagement.dto;

import lombok.Data;

@Data
public class AddAdminRequest {
    private String name; // This will be split into firstName and lastName, or used as full name
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
}

