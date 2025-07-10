package com.pavelryzh.provider.config;

import com.pavelryzh.provider.service.JwtService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(HandlerExceptionResolver handlerExceptionResolver, JwtService jwtService, UserDetailsService userDetailsService) {
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userLogin;

        // 1. Наличие и формат заголовка
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Извлечение токена
        jwt = authHeader.substring(7);

        // 3. Извлечение логина из токена
        userLogin = jwtService.extractUsername(jwt);

        // 4. Пользователь существует и еще не аутентифицирован в текущем контексте
        if (userLogin != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userLogin);

            // 5. Валидность токена
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // 6. Сохраняем аутентификацию в контексте безопасности
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
