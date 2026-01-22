package com.example.salesboard.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateUserRequest {
    @NotBlank
    private String username;
    @Email
    private String email;
}
