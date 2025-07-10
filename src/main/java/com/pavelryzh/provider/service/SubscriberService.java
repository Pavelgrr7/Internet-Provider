package com.pavelryzh.provider.service;

import com.pavelryzh.provider.dto.subscriber.SubscriberCreateDto;
import com.pavelryzh.provider.dto.subscriber.SubscriberResponseDto;

public interface SubscriberService {
    SubscriberResponseDto createSubscriber(SubscriberCreateDto createDto);
    SubscriberResponseDto getById(String id);
}
