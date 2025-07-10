package com.pavelryzh.provider.service;

import com.pavelryzh.provider.dto.auth.AuthResponseDto;
import jakarta.validation.constraints.NotBlank;

public interface AuthService {
    AuthResponseDto attemptLogin(@NotBlank(message = "Логин не может быть пустым") String login, @NotBlank(message = "Пароль не может быть пустым") String password);
}
