package com.pavelryzh.provider.controller;


import com.pavelryzh.provider.dto.subscriber.EmailChangeDto;
import com.pavelryzh.provider.dto.subscriber.PasswordChangeDto;
import com.pavelryzh.provider.dto.subscriber.SubscriberResponseDto;
import com.pavelryzh.provider.model.Subscriber;
import com.pavelryzh.provider.service.SubscriberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    SubscriberService subscriberService;

    public UserController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SubscriberResponseDto> getUserData(Authentication authentication) {
        Subscriber currentUser = (Subscriber) authentication.getPrincipal();

        log.info("profile requested for user: {}", currentUser.getAuthorities());
        SubscriberResponseDto dto = subscriberService.getById(currentUser.getId());
        log.info("response: {}", dto.toString());
        return ResponseEntity.ok(dto);
    }
    @PatchMapping("/my/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(
            Authentication authentication,
            @Valid @RequestBody PasswordChangeDto passwordChangeDto) {

        Subscriber currentUser = (Subscriber) authentication.getPrincipal();
        subscriberService.changePassword(currentUser.getId(), passwordChangeDto);

        return ResponseEntity.ok().build(); // Возвращаем 200 OK без тела
    }

    @PatchMapping("/my/email")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changeEmail(
            Authentication authentication,
            @Valid @RequestBody EmailChangeDto emailChangeDto) {

        Subscriber currentUser = (Subscriber) authentication.getPrincipal();
        subscriberService.changeEmail(currentUser.getId(), emailChangeDto.getNewEmail());

        return ResponseEntity.ok().build();
    }
}
