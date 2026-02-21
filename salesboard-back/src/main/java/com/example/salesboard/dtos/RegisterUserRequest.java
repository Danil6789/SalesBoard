package com.example.salesboard.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotBlank(message="this is empty")
    @Size(max = 40, message = "this name is more 40 letters")
    private String username;
    @Email(message = "invalid email")
    @NotBlank(message = "email is empty")
    private String email;
    @Size(min = 6, max = 20, message = "this password must be between 6 to 20")
    @NotBlank(message = "password is required")
    private String password;
}
