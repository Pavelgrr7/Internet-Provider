package com.pavelryzh.provider.service.impl;

import com.pavelryzh.provider.dto.mapper.AdditionalServiceMapper;
import com.pavelryzh.provider.dto.service.AdditionalServiceResponseDto;
import com.pavelryzh.provider.dto.tariff.TariffCreateDto;
import com.pavelryzh.provider.dto.tariff.TariffResponseDto;
import com.pavelryzh.provider.dto.tariff.TariffSelectionDto;
import com.pavelryzh.provider.dto.tariff.TariffUpdateDto;
import com.pavelryzh.provider.exception.ResourceNotFoundException;
import com.pavelryzh.provider.model.AdditionalService;
import com.pavelryzh.provider.model.Contract;
import com.pavelryzh.provider.model.Tariff;
import com.pavelryzh.provider.repository.AdditionalServiceRepository;
import com.pavelryzh.provider.repository.ContractRepository;
import com.pavelryzh.provider.repository.ServiceRepository;
import com.pavelryzh.provider.repository.TariffRepository;
import com.pavelryzh.provider.service.TariffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;
    private final ContractRepository contractRepository;
    private final ServiceRepository serviceRepository;

    public TariffServiceImpl(TariffRepository tariffRepository, ContractRepository contractRepository, ServiceRepository serviceRepository, AdditionalServiceRepository additionalServiceRepository) {
        this.tariffRepository = tariffRepository;
        this.contractRepository = contractRepository;
        this.serviceRepository = serviceRepository;
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

        if (createDto.getServiceIds() != null && !createDto.getServiceIds().isEmpty()) {
            List<AdditionalService> servicesToLink = serviceRepository.findAllById(createDto.getServiceIds());
            tariff.setAvailableServices(servicesToLink);
        }
        log.info("tried to create tariff: {}", tariff);
        Tariff savedTariff = tariffRepository.save(tariff);



        return toResponseDto(savedTariff);
    }

    /**
     * Метод для получения тарифа по ID
     */

    @Override
    @Transactional(readOnly = true)
    public TariffResponseDto getById(Long id) {
        // --- Взаимодействие с репозиторием ---
        // искл, если сущность не найдена.
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тариф с ID " + id + " не найден."));

        // --- Маппинг из Entity в Response DTO ---
        return toResponseDto(tariff);
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional
    public void remove(Long id) {
        if (contractRepository.existsByTariffId(id)) {

            throw new DataIntegrityViolationException(
                    "Невозможно удалить тариф, так как на него ссылаются существующие договоры. " +
                            "Сначала переведите абонентов на другие тарифы."
            );
        }

        tariffRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdditionalServiceResponseDto> getAvailableServicesForContract(Long contractId) {
        // 1. ВСЕ услуги, доступные для данного тарифа

        var tariffId = contractRepository.findById(contractId).orElseThrow(() -> new ResourceNotFoundException("Контракт с ID " + contractId + " не найден."))
                .getTariff().getId();

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
    public List<TariffSelectionDto> findActiveTariffs(Integer year) {
        if (year == null) {
            return tariffRepository.findAll().stream()
                    .map(this::toSelectionDto)
                    .toList();
        } else {
            return tariffRepository.findAllActiveByYear(year)
                    .stream()
                    .map(this::toSelectionDto)
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true) // Добавляем транзакцию, т.к. есть обращение к БД
    public List<Integer> findActiveYearsForTariff(Long tariffId) {
        // 1. Получаем дату начала действия тарифа
        LocalDate startDate = tariffRepository.findById(tariffId)
                .orElseThrow(() -> new ResourceNotFoundException("Тариф с ID " + tariffId + " не найден."))
                .getStartDate();

        int startYear = startDate.getYear();
        int currentYear = Year.now().getValue(); // Более правильный способ получить текущий год

        // 2. Генерируем поток целых чисел от startYear до currentYear (включительно)
        return IntStream.rangeClosed(startYear, currentYear)
                .boxed() // 3. Превращаем примитивный IntStream в Stream<Integer>
                .sorted((y1, y2) -> y2.compareTo(y1)) // 4. Сортируем в обратном порядке (от нового к старому)
                .collect(Collectors.toList()); // 5. Собираем результат в список
    }

    @Override
    @Transactional
    public List<TariffSelectionDto> findTariffsForSelection() {
        return tariffRepository.findAll()
                .stream()
                .map(this::toSelectionDto)
                .toList();
    }

    @Override
    public void addServiceToTariff(Long tariffId, Long serviceId) {

        Tariff selectedTariff = tariffRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Тариф не найден."));

        AdditionalService newService = serviceRepository.findByServiceId(serviceId).orElseThrow(() -> new ResourceNotFoundException("Услуга не найдена."));
        List<AdditionalService> newServices = selectedTariff.getAvailableServices();
        newServices.add(newService);

        selectedTariff.setAvailableServices(newServices);
    }


    @Override
    public void removeServiceFromTariff(Long tariffId, Long serviceId) {

        Tariff selectedTariff = tariffRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Тариф не найден."));

        var newServices = selectedTariff.getAvailableServices().stream()
                .filter(service ->
                        !Objects.equals(service.getServiceId(), serviceId)
                )
                .toList();

        selectedTariff.setAvailableServices(newServices);
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
        dto.setAvailableServices(
                AdditionalServiceMapper.toDtoList(tariff.getAvailableServices())
        );

        return dto;
    }

    private Tariff toEntity(TariffCreateDto createDto) {
        Tariff tariff = new Tariff();
        tariff.setName(createDto.getName());
        tariff.setDeclaredSpeed(createDto.getDeclaredSpeed());
        tariff.setInstallationFee(createDto.getInstallationFee());
        tariff.setIpAddressType(createDto.getIpAddressType());
        tariff.setStartDate(createDto.getStartDate());

//        if (createDto.getServiceIds() != null && !createDto.getServiceIds().isEmpty()) {
//            List<AdditionalService> services = createDto.getServiceIds().stream()
//                    .map(serviceId -> serviceRepository.findById(serviceId)
//                            .orElseThrow(() -> new ResourceNotFoundException("Услуга с ID " + serviceId + " не найдена")))
//                    .collect(Collectors.toList());
//
//            tariff.setAvailableServices(services);
//        }
        return tariff;
    }

    private TariffSelectionDto toSelectionDto(Tariff tariff) {
        return new TariffSelectionDto(
                tariff.getId(),
                tariff.getName());
    }
}