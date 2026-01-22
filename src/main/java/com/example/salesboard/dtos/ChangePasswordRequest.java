package com.example.salesboard.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChangePasswordRequest {
    @Size(min = 6)
    private String oldPassword;
    @Size(min = 6)
    private String newPassword;
}
