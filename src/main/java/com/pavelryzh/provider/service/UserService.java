package com.pavelryzh.provider.service;

import com.pavelryzh.provider.dto.user.PasswordChangeDto;
import com.pavelryzh.provider.dto.user.admin.AdminResponseDto;
import com.pavelryzh.provider.dto.user.admin.AdminSubscriberDetailsDto;
import com.pavelryzh.provider.dto.user.subscriber.SubscriberCreateDto;
import com.pavelryzh.provider.dto.user.subscriber.SubscriberListItemDto;
import com.pavelryzh.provider.dto.user.subscriber.SubscriberResponseDto;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface UserService {
    SubscriberResponseDto createSubscriber(SubscriberCreateDto createDto);
    SubscriberResponseDto getSubscriberById(Long id);

    List<SubscriberListItemDto> getAllSubscribers();

    AdminResponseDto getAdminById(Long id);
    void changeEmail(long id, String email);
    void changePassword(long id, PasswordChangeDto passwordChangeDto);

    AdminSubscriberDetailsDto getSubscriberDetailsById(Long id);

    void updateFullName(Long id, @NotBlank String value);

    void updateEmail(Long id, @NotBlank String value);

    void updatePhoneNumber(Long id, @NotBlank String value);

    void updatePassport(Long id, @NotBlank String value);

    void updateLogin(Long id, @NotBlank String value);
}
