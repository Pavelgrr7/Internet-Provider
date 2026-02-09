package com.pavelryzh.provider.controller;

import com.pavelryzh.provider.dto.auth.AuthResponseDto;
import com.pavelryzh.provider.dto.auth.LoginRequestDto;
import com.pavelryzh.provider.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        // Сервис аутентификации проверит данные и в случае успеха вернет DTO с токеном
        log.info("Попытка аутентификации: {}", loginRequest);
        AuthResponseDto authResponse = authService.attemptLogin(loginRequest.getLogin(), loginRequest.getPassword());
        log.info("Ответ: {}", authResponse);
        return ResponseEntity.ok(authResponse);
    }
}
