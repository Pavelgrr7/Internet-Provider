package com.pavelryzh.provider.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailChangeDto {
    @NotBlank
    @Email(message = "Некорректный формат email")
    private String newEmail;
}