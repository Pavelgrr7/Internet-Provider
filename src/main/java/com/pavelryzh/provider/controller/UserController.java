package com.pavelryzh.provider.controller;


import com.pavelryzh.provider.dto.user.EmailChangeDto;
import com.pavelryzh.provider.dto.user.PasswordChangeDto;
import com.pavelryzh.provider.model.Administrator;
import com.pavelryzh.provider.model.Subscriber;
import com.pavelryzh.provider.model.User;
import com.pavelryzh.provider.service.ContractService;
import com.pavelryzh.provider.service.UserService;
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

//    SubscriberService subscriberService;

    private final UserService userService;
//    private final ContractService contractService;

    public UserController(UserService userService
//            ,
//                          ContractService contractService
    ) {
        this.userService = userService;
//        this.contractService = contractService;
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
}
