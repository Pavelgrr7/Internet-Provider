package com.pavelryzh.provider.controller;

import com.pavelryzh.provider.dto.auth.AuthResponseDto;
import com.pavelryzh.provider.dto.auth.LoginRequestDto;
import com.pavelryzh.provider.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest, HttpServletRequest request) {
        // Сервис аутентификации проверит данные и в случае успеха вернет DTO с токеном
        log.info("Попытка аутентификации от IP: {}. Логин: {}", request.getRemoteAddr(), loginRequest.getLogin());
        AuthResponseDto authResponse = authService.attemptLogin(loginRequest.getLogin(), loginRequest.getPassword());
        return ResponseEntity.ok(authResponse);
    }
}
