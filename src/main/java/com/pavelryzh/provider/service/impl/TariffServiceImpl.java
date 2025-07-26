package com.pavelryzh.provider.service.impl;

import com.pavelryzh.provider.dto.mapper.AdditionalServiceMapper;
import com.pavelryzh.provider.dto.service.AdditionalServiceResponseDto;
import com.pavelryzh.provider.dto.tariff.TariffCreateDto;
import com.pavelryzh.provider.dto.tariff.TariffResponseDto;
import com.pavelryzh.provider.dto.tariff.TariffUpdateDto;
import com.pavelryzh.provider.exception.ResourceNotFoundException;
import com.pavelryzh.provider.model.AdditionalService;
import com.pavelryzh.provider.model.Contract;
import com.pavelryzh.provider.model.Tariff;
import com.pavelryzh.provider.repository.ContractRepository;
import com.pavelryzh.provider.repository.TariffRepository;
import com.pavelryzh.provider.service.TariffService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;
    private final ContractRepository contractRepository;
    public TariffServiceImpl(TariffRepository tariffRepository, ContractRepository contractRepository) {
        this.tariffRepository = tariffRepository;
        this.contractRepository = contractRepository;
    }

    /**
     * Метод для создания нового тарифа
     */

    @Override
    @Transactional // изменение данных в бд -> transactional
    public TariffResponseDto create(TariffCreateDto createDto) {

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
    public TariffResponseDto getById(Long id) {
        // --- Взаимодействие с репозиторием ---
        // искл, если сущность не найдена.
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тариф с ID " + id + " не найден."));

        // --- Маппинг из Entity в Response DTO ---
        return toResponseDto(tariff);
    }

    @Override
    public List<TariffResponseDto> getAll() {

        List<Tariff> tariffs = tariffRepository.findAll();

        return tariffs.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Метод для обновления тарифа
     */

    @Override
    @Transactional
    public TariffResponseDto update(Long id, TariffUpdateDto updateDto) {

        Tariff existingTariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тариф с ID " + id + " не найден."));

        if (updateDto.getName() != null) {
            existingTariff.setName(updateDto.getName());
        }
        if (updateDto.getDeclaredSpeed() != null) {
            existingTariff.setDeclaredSpeed(updateDto.getDeclaredSpeed());
        }
        if (updateDto.getInstallationFee() != null) {
            existingTariff.setInstallationFee(updateDto.getInstallationFee());
        }
        if (updateDto.getIpAddressType() != null) {
            existingTariff.setIpAddressType(updateDto.getIpAddressType());
        }
        if (updateDto.getStartDate() != null) {
            existingTariff.setStartDate(updateDto.getStartDate());
        }

        // JPA SQL UPDATE.
        Tariff savedTariff = tariffRepository.save(existingTariff);

        return toResponseDto(savedTariff);
    }

    /**
     * Метод для удаления тарифа
     */

    @Override
    public void remove(Long id) {
        tariffRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdditionalServiceResponseDto> getAvailableServicesForContract(Long contractId) {
        // 1. ВСЕ услуги, доступные для данного тарифа

        var tariffId = contractRepository.findById(contractId).orElseThrow(() -> new ResourceNotFoundException("Контракт с ID " + contractId + " не найден."))
                .getTariffId();

        List<AdditionalService> allServicesForTariff = tariffRepository.findByIdWithAvailableServices(tariffId)
                .orElseThrow(() -> new ResourceNotFoundException("Тариф с ID " + tariffId + " не найден."))
                .getAvailableServices();

        // 2. Получаем ВСЕ услуги, УЖЕ ПОДКЛЮЧЕННЫЕ к данному договору
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Договор не найден"));
        Set<Long> connectedServiceIds = contract.getServices().stream()
                .map(AdditionalService::getServiceId)
                .collect(Collectors.toSet());

        // 3. Фильтруем и возвращаем только те, которые еще не подключены
        List<AdditionalService> availableServices = allServicesForTariff.stream()
                .filter(service -> !connectedServiceIds.contains(service.getServiceId()))
                .collect(Collectors.toList());

        // 4. Маппим результат в DTO
        return AdditionalServiceMapper.toDtoList(availableServices);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdditionalServiceResponseDto> getAvailableServicesByTariffId(Long tariffId) {
        // 1. Находим тариф вместе с его услугами ОДНИМ запросом
        Tariff tariff = tariffRepository.findByIdWithAvailableServices(tariffId)
                .orElseThrow(() -> new ResourceNotFoundException("Тариф с ID " + tariffId + " не найден."));

        // 2. Просто возвращаем уже загруженный список услуг, предварительно смапив его
        return AdditionalServiceMapper.toDtoList(tariff.getAvailableServices());
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