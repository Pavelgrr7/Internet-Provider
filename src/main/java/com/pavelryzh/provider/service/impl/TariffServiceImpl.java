package com.pavelryzh.provider.service.impl;

import com.pavelryzh.provider.dto.tariff.TariffCreateDto;
import com.pavelryzh.provider.dto.tariff.TariffResponseDto;
import com.pavelryzh.provider.exception.ResourceNotFoundException;
import com.pavelryzh.provider.model.Tariff;
import com.pavelryzh.provider.repository.TariffRepository;
import com.pavelryzh.provider.service.TariffService;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;

    public TariffServiceImpl(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    /**
     * Метод для создания нового тарифа
     */

    @Override
    @Transactional // изменение данных в бд -> transactional
    public TariffResponseDto create(TariffCreateDto createDto) {
        // --- Бизнес-логика и валидация ---
        // временно для примера проверка существования тарифа с таким именем.
        if (tariffRepository.existsByName(createDto.getName())) {
            throw new IllegalArgumentException("Тариф с названием '" + createDto.getName() + "' уже существует.");
        }

//         todo сделать Enum
        if (!("Динамический".equals(createDto.getIpAddressType()) || "Статический".equals(createDto.getIpAddressType()))) {
            throw new IllegalArgumentException("Неверный тип IP адреса: " + createDto.getIpAddressType());
        }

        // --- Маппинг из DTO в Entity ---
        Tariff tariff = toEntity(createDto);

        // --- Взаимодействие с репозиторием ---
        Tariff savedTariff = tariffRepository.save(tariff);

        // --- Маппинг из Entity в Response DTO ---
        return toResponseDto(savedTariff);
    }

    /**
     * Метод для получения тарифа по ID
     */

    @Override
    public TariffResponseDto getById(Long id) throws ResourceNotFoundException {
        // --- Взаимодействие с репозиторием ---
        // искл. если сущность не найдена.
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тариф с ID " + id + " не найден."));

        // --- Маппинг из Entity в Response DTO ---
        return toResponseDto(tariff);
    }


    private TariffResponseDto toResponseDto(Tariff tariff) {
        TariffResponseDto dto = new TariffResponseDto();
        dto.setId(tariff.getId());
        dto.setName(tariff.getName());
        dto.setDeclaredSpeed(tariff.getDeclaredSpeed());
        dto.setInstallationFee(tariff.getInstallationFee());
        dto.setIpAddressType(tariff.getIpAddressType());
        dto.setStartDate(tariff.getStartDate());
        return dto;
    }

    private Tariff toEntity(TariffCreateDto createDto) {
        Tariff tariff = new Tariff();
        tariff.setName(createDto.getName());
        tariff.setDeclaredSpeed(createDto.getDeclaredSpeed());
        tariff.setInstallationFee(createDto.getInstallationFee());
        tariff.setIpAddressType(createDto.getIpAddressType());
        tariff.setStartDate(createDto.getStartDate());
        return tariff;
    }
}