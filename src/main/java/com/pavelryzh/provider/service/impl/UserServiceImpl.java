package com.pavelryzh.provider.service.impl;

import com.pavelryzh.provider.dto.user.PasswordChangeDto;
import com.pavelryzh.provider.dto.user.admin.AdminResponseDto;
import com.pavelryzh.provider.dto.user.subscriber.SubscriberCreateDto;
import com.pavelryzh.provider.dto.user.subscriber.SubscriberResponseDto;
import com.pavelryzh.provider.exception.AuthException;
import com.pavelryzh.provider.exception.ResourceNotFoundException;
import com.pavelryzh.provider.model.Administrator;
import com.pavelryzh.provider.model.Subscriber;
import com.pavelryzh.provider.model.User;
import com.pavelryzh.provider.repository.UserRepository;
import com.pavelryzh.provider.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
        }

        public SubscriberResponseDto createSubscriber(SubscriberCreateDto createDto) {

            Subscriber subscriber = new Subscriber();
            subscriber.setLogin(createDto.getLogin());

            String hashedPassword = passwordEncoder.encode(createDto.getRawPassword());
            subscriber.setPasswordHash(hashedPassword);

            subscriber.setRole("ROLE_USER"); // todo сделать по-человечески

            Subscriber savedSubscriber = userRepository.save(subscriber);

            return toSubscriberDto(savedSubscriber);
        }

    @Override
    @Transactional( /*readOnly = true*/)
    public SubscriberResponseDto getSubscriberById(Long id) {
        User subscriber = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + id + " не найден."));
        if (!(subscriber instanceof Subscriber)) {
            throw new IllegalArgumentException("Пользователь с ID " + id + " не является абонентом.");
        }

        return toSubscriberDto((Subscriber) subscriber);
    }


    @Override
    public AdminResponseDto getAdminById(Long id) {
            User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + id + "не найден."));

            if (!(user instanceof Administrator)) {
                throw new IllegalArgumentException("Пользователь с ID " + id + " не является администратором.");
            }

            return toAdminDto((Administrator) user);
    }

    @Override
    @Transactional
    public void changeEmail(long id, String email) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + id + "не найден."));
        user.setEmail(email);
    }

    @Override
    @Transactional
    public void changePassword(long id, PasswordChangeDto dto) {
            User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + id + "не найден."));

        String currentPasswordHash = passwordEncoder.encode(dto.getCurrentPassword());
        if (currentPasswordHash.equals(user.getPassword())) {
            String hashedPassword = passwordEncoder.encode(dto.getNewPassword());
            user.setPasswordHash(hashedPassword);
        } else throw new AuthException("Неверный пароль.");
    }


    private SubscriberResponseDto toSubscriberDto(Subscriber savedSubscriber) {
            SubscriberResponseDto responseDto = new SubscriberResponseDto();
            responseDto.setSubscriberId(savedSubscriber.getId());
            responseDto.setFirstName(savedSubscriber.getFirstName());
            responseDto.setMiddleName(savedSubscriber.getMiddleName());
            responseDto.setLastName(savedSubscriber.getLastName());
            responseDto.setEmail(savedSubscriber.getEmail());
            responseDto.setLogin(savedSubscriber.getLogin());
            return responseDto;
    }

    private AdminResponseDto toAdminDto(Administrator savedAdmin) {
            AdminResponseDto responseDto = new AdminResponseDto();
            responseDto.setUserId(savedAdmin.getId());
            responseDto.setFirstName(savedAdmin.getFirstName());
            responseDto.setMiddleName(savedAdmin.getMiddleName());
            responseDto.setLastName(savedAdmin.getLastName());
            responseDto.setEmail(savedAdmin.getEmail());
            responseDto.setLogin(savedAdmin.getLogin());
            return responseDto;
    }
}
