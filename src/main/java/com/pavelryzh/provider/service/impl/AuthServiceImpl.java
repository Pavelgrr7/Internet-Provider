package com.pavelryzh.provider.service.impl;

import com.pavelryzh.provider.dto.auth.AuthResponseDto;
import com.pavelryzh.provider.service.AuthService;
import com.pavelryzh.provider.service.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponseDto attemptLogin(String login, String password) {
        // Шаг 1: Аутентификация.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password)
        );

        // Шаг 2: Получение UserDetails из объекта Authentication.
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Шаг 3: Генерация токена
        String token = jwtService.generateToken(userDetails);

        // Шаг 4: Получение роли и логина для ответа.
        String userRole = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_UNDEFINED");
        String userLogin = userDetails.getUsername();

        return new AuthResponseDto(token, userRole, userLogin);
    }
}
