package com.pavelryzh.provider.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}
