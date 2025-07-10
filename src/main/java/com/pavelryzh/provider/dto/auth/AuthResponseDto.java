package com.pavelryzh.provider.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private String role;
    private String login;
}
