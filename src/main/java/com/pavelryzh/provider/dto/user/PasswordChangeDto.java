package com.pavelryzh.provider.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordChangeDto {
    @NotBlank
    private String currentPassword;
    @NotBlank
    @Size(min = 6, message = "Новый пароль должен содержать минимум 6 символов")
    private String newPassword;
}
