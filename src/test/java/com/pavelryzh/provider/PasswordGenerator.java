package com.pavelryzh.provider;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "";
        String hashedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("Пароль: " + rawPassword);
        System.out.println("Хэш для вставки в БД: " + hashedPassword);
    }
}
