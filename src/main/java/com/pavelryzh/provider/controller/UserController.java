package com.pavelryzh.provider.controller;

import com.pavelryzh.provider.dto.CreateFullPackageDto;
import com.pavelryzh.provider.dto.UpdateFieldRequestDto;
import com.pavelryzh.provider.dto.user.EmailChangeDto;
import com.pavelryzh.provider.dto.user.PasswordChangeDto;
import com.pavelryzh.provider.dto.user.admin.AdminSubscriberDetailsDto;
import com.pavelryzh.provider.dto.user.subscriber.SubscriberListItemDto;
import com.pavelryzh.provider.model.Administrator;
import com.pavelryzh.provider.model.Subscriber;
import com.pavelryzh.provider.model.User;
import com.pavelryzh.provider.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserData(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser instanceof Subscriber subscriber) {
            var dto = userService.getSubscriberById(subscriber.getId());
            return ResponseEntity.ok(dto);

        } else if (currentUser instanceof Administrator admin) {
            var dto = userService.getAdminById(admin.getId());
            return ResponseEntity.ok(dto);

        } else return ResponseEntity.badRequest().build();
    }

    @PostMapping("/create-full")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminSubscriberDetailsDto> createSubscriberAndFirstContract(
            @Valid @RequestBody CreateFullPackageDto fullPackage) {
        log.info("new full-package request: {}", fullPackage);

        AdminSubscriberDetailsDto newSubscriber = userService.createSubscriberAndFirstContract(fullPackage);


        return ResponseEntity.status(HttpStatus.CREATED).body(newSubscriber);
    }

    @PatchMapping("/my/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(
            Authentication authentication,
            @Valid @RequestBody PasswordChangeDto passwordChangeDto) {

        User currentUser = (User) authentication.getPrincipal();
        userService.changePassword(currentUser.getId(), passwordChangeDto);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/my/email")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changeEmail(
            Authentication authentication,
            @Valid @RequestBody EmailChangeDto emailChangeDto) {

        User currentUser = (User) authentication.getPrincipal();
        userService.changeEmail(currentUser.getId(), emailChangeDto.getNewEmail());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/subscribers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SubscriberListItemDto>> getAllSubscribers() {
        List<SubscriberListItemDto> subscribers = userService.getAllSubscribers();
        log.info("sent subscribers: {}", subscribers);
        return ResponseEntity.ok(subscribers);
    }

    @GetMapping("/subscribers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminSubscriberDetailsDto> getSubscriberDetails(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getSubscriberDetailsById(id));
    }

    @PatchMapping("/subscribers/{id}/fullname")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateFullName(@PathVariable Long id, @Valid @RequestBody UpdateFieldRequestDto request) {
        log.info("request to change name: {}", request);
        userService.updateFullName(id, request.getValue());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/subscribers/{id}/email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateEmail(@PathVariable Long id, @Valid @RequestBody UpdateFieldRequestDto request) {
        userService.updateEmail(id, request.getValue());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/subscribers/{id}/login")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateLogin(@PathVariable Long id, @Valid @RequestBody UpdateFieldRequestDto request) {
        userService.updateLogin(id, request.getValue());
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/subscribers/{id}/passport")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updatePassport(@PathVariable Long id, @Valid @RequestBody UpdateFieldRequestDto request) {
        userService.updatePassport(id, request.getValue());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/subscribers/{id}/phone")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updatePhoneNumber(@PathVariable Long id, @Valid @RequestBody UpdateFieldRequestDto request) {
        userService.updatePhoneNumber(id, request.getValue());
        return ResponseEntity.noContent().build();
    }




}

