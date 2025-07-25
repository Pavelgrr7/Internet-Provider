package com.pavelryzh.provider.service.impl;

import com.pavelryzh.provider.dto.subscriber.PasswordChangeDto;
import com.pavelryzh.provider.dto.subscriber.SubscriberCreateDto;
import com.pavelryzh.provider.dto.subscriber.SubscriberResponseDto;
import com.pavelryzh.provider.exception.AuthException;
import com.pavelryzh.provider.exception.ResourceNotFoundException;
import com.pavelryzh.provider.model.Subscriber;
import com.pavelryzh.provider.repository.SubscriberRepository;
import com.pavelryzh.provider.service.SubscriberService;
import jakarta.transaction.Transactional;
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
    @Transactional( /*readOnly = true*/)
    public SubscriberResponseDto getById(long id) {
        Subscriber subscriber = subscriberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + id + " не найден."));
        return toResponseDto(subscriber);
    }

    @Override
    @Transactional
    public void changeEmail(long id, String email) {
        Subscriber subscriber = subscriberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + id + "не найден."));
        subscriber.setEmail(email);
    }

    @Override
    @Transactional
    public void changePassword(long id, PasswordChangeDto dto) {
            Subscriber subscriber = subscriberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + id + "не найден."));

        String currentPasswordHash = passwordEncoder.encode(dto.getCurrentPassword());
        if (currentPasswordHash.equals(subscriber.getPassword())) {
            String hashedPassword = passwordEncoder.encode(dto.getNewPassword());
            subscriber.setPasswordHash(hashedPassword);
        } else throw new AuthException("Неверный пароль.");
    }


    private SubscriberResponseDto toResponseDto(Subscriber savedSubscriber) {
            SubscriberResponseDto responseDto = new SubscriberResponseDto();
            responseDto.setSubscriberId(savedSubscriber.getId());
            responseDto.setFirstName(savedSubscriber.getFirstName());
            responseDto.setMiddleName(savedSubscriber.getMiddleName());
            responseDto.setLastName(savedSubscriber.getLastName());
            responseDto.setEmail(savedSubscriber.getEmail());
            responseDto.setLogin(savedSubscriber.getLogin());
            responseDto.setRole(savedSubscriber.getRole());
            return responseDto;
    }
}
