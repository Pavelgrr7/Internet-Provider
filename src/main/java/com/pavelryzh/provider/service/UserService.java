package com.pavelryzh.provider.service;

import com.pavelryzh.provider.dto.user.PasswordChangeDto;
import com.pavelryzh.provider.dto.user.admin.AdminResponseDto;
import com.pavelryzh.provider.dto.user.subscriber.SubscriberCreateDto;
import com.pavelryzh.provider.dto.user.subscriber.SubscriberResponseDto;

public interface UserService {
    SubscriberResponseDto createSubscriber(SubscriberCreateDto createDto);
    SubscriberResponseDto getSubscriberById(Long id);

    AdminResponseDto getAdminById(Long id);
    void changeEmail(long id, String email);
    void changePassword(long id, PasswordChangeDto passwordChangeDto);
}
