package com.pavelryzh.provider.controller;

import com.pavelryzh.provider.dto.auth.AuthResponseDto;
import com.pavelryzh.provider.dto.auth.LoginRequestDto;
import com.pavelryzh.provider.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;


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
        AuthResponseDto authResponse = authService.attemptLogin(loginRequest.getLogin(), loginRequest.getPassword());
        return ResponseEntity.ok(authResponse);
    }
}
