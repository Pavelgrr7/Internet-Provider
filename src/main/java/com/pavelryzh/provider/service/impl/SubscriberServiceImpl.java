package com.pavelryzh.provider.service.impl;

import com.pavelryzh.provider.dto.subscriber.SubscriberCreateDto;
import com.pavelryzh.provider.dto.subscriber.SubscriberResponseDto;
import com.pavelryzh.provider.model.Subscriber;
import com.pavelryzh.provider.repository.SubscriberRepository;
import com.pavelryzh.provider.service.SubscriberService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SubscriberServiceImpl implements SubscriberService {

        private final SubscriberRepository subscriberRepository;
        private final PasswordEncoder passwordEncoder;

        public SubscriberServiceImpl(SubscriberRepository subscriberRepository, PasswordEncoder passwordEncoder) {
            this.subscriberRepository = subscriberRepository;
            this.passwordEncoder = passwordEncoder;
        }

        public SubscriberResponseDto createSubscriber(SubscriberCreateDto createDto) {

            Subscriber subscriber = new Subscriber();
            subscriber.setLogin(createDto.getLogin());

            String hashedPassword = passwordEncoder.encode(createDto.getRawPassword());
            subscriber.setPasswordHash(hashedPassword);

            subscriber.setRole("ROLE_USER"); // todo сделать по-человечески

            Subscriber savedSubscriber = subscriberRepository.save(subscriber);

            return toResponseDto(savedSubscriber);
        }

    @Override
    public SubscriberResponseDto getById(String id) {
        return toResponseDto(
                subscriberRepository.getReferenceById(
                        Long.parseLong(id)
                ));
    }

    private SubscriberResponseDto toResponseDto(Subscriber savedSubscriber) {
            SubscriberResponseDto responseDto = new SubscriberResponseDto();
            responseDto.setSubscriberId(savedSubscriber.getId());
            responseDto.setFirstName(savedSubscriber.getFirstName());
            responseDto.setMiddleName(savedSubscriber.getMiddleName());
            responseDto.setLastName(savedSubscriber.getLastName());
            responseDto.setLogin(savedSubscriber.getLogin());
            responseDto.setRole(savedSubscriber.getRole());
            return responseDto;
    }
}
