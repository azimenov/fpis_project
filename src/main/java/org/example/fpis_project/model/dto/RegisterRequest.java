package org.example.fpis_project.model.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String phoneNumber;
    private String username;
    private String password;
}
