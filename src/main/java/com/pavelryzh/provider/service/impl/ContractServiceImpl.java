package com.pavelryzh.provider.service.impl;

import com.pavelryzh.provider.dto.contract.ContractCreateDto;
import com.pavelryzh.provider.dto.contract.ContractResponseDto;
import com.pavelryzh.provider.dto.contract.ContractWithServicesDto;
import com.pavelryzh.provider.dto.mapper.AdditionalServiceMapper;
import com.pavelryzh.provider.dto.tariff.ChangeTariffPreviewDto;
import com.pavelryzh.provider.dto.user.subscriber.ContractInfo;
import com.pavelryzh.provider.exception.ResourceNotFoundException;
import com.pavelryzh.provider.model.AdditionalService;
import com.pavelryzh.provider.model.Contract;
import com.pavelryzh.provider.model.Subscriber;
import com.pavelryzh.provider.model.Tariff;
import com.pavelryzh.provider.repository.AdditionalServiceRepository;
import com.pavelryzh.provider.repository.ContractRepository;
import com.pavelryzh.provider.repository.TariffRepository;
import com.pavelryzh.provider.repository.UserRepository;
import com.pavelryzh.provider.service.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContractServiceImpl implements ContractService {

    private final AdditionalServiceRepository additionalServiceRepository;
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final TariffRepository tariffRepository;

    public ContractServiceImpl(ContractRepository contractRepository, UserRepository userRepository, TariffRepository tariffRepository, AdditionalServiceRepository additionalServiceRepository) {
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
        this.tariffRepository = tariffRepository;
        this.additionalServiceRepository = additionalServiceRepository;
    }

    /**
     * Центральный метод для пересчета и сохранения месячной платы.
     * Является приватным, т.к. это внутренняя логика сервиса.
     * @param contract Сущность договора, для которой нужно выполнить пересчет.
     */
    private void recalculateAndSetMonthlyFee(Contract contract) {
        // 1. Берем базовую стоимость из тарифа
        // Предполагаем, что у тарифа есть поле monthlyCost
        Tariff currentTariff = tariffRepository.findById(contract.getTariff().getId()).orElseThrow(() -> new ResourceNotFoundException("В договоре указан несуществующий тариф."));
        BigDecimal tariffCost = currentTariff.getInstallationFee();

        // 2. Считаем стоимость всех подключенных услуг
        BigDecimal servicesCost = contract.getServices().stream()
                .map(AdditionalService::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Складываем и устанавливаем новое значение
        BigDecimal newMonthlyFee = tariffCost.add(servicesCost);
        contract.setMonthlyFee(newMonthlyFee);

        // 4. Сохраняем изменения в БД
        contractRepository.save(contract);
    }


    @Transactional
    public ContractResponseDto create(ContractCreateDto createDto) {
        // 1. Находим связанные сущности
        Subscriber subscriber = (Subscriber) userRepository.findById(createDto.getSubscriberId()).orElseThrow();
        Tariff tariff = tariffRepository.findById(createDto.getTariffId()).orElseThrow();

        // 2. Создаем новый контракт
        Contract contract = new Contract();
        contract.setSubscriber(subscriber);
        contract.setTariff(tariff);
        contract.setContractNumber(createDto.getContractNumber());
        contract.setServiceAddress(createDto.getServiceAddress());
        contract.setSigningDate(createDto.getSigningDate());
        contract.setServiceStartDate(createDto.getServiceStartDate());

        // 3. Рассчитываем начальную monthlyFee (стоимость тарифа)
        contract.setMonthlyFee(tariff.getInstallationFee());

        // 4. Сохраняем и возвращаем
        Contract savedContract = contractRepository.save(contract);
        return toResponseDto(savedContract);
    }

    @Override
    public List<ContractResponseDto> getAll() {
        return  contractRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<ContractResponseDto> getContractsByUserId(long id) {
        return contractRepository.findBySubscriber_Id(id).stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public ContractResponseDto getContractDtoById(Long contractId) {
        return toResponseDto(contractRepository.findById(contractId).orElseThrow(() -> new ResourceNotFoundException("Контракт с ID " + contractId + " не найден.")));
    }

    @Override
    public Map<Long, List<ContractInfo>> getContractInfoForUserIds(List<Long> subscriberIds) {
        Map<Long, List<ContractInfo>> contractsInfoMap = new HashMap<>();
        for (Long subscriberId : subscriberIds) {
            List<ContractInfo> infoList = new ArrayList<>();
            List<Contract> contracts = contractRepository.findBySubscriber_Id(subscriberId);
            contracts.forEach(
                    contract -> infoList.add(
                            new ContractInfo(
                                    contract.getId(),
                                    contract.getContractNumber()
                            )
                    )
            );
            contractsInfoMap.put(subscriberId, infoList);
        }
        return contractsInfoMap;
    }

    @Transactional
    public void removeServiceFromContract(Long userId, Long contractId, Long serviceId) {
        // 1. Найти договор и убедиться, что он принадлежит текущему пользователю

        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Договор не найден"));

        if (!Objects.equals(userRepository.findById(userId).orElseThrow().getRole(), "ROLE_ADMIN")) {
            if (!contract.getSubscriber().getId().equals(userId)) {
                throw new AccessDeniedException("Вы не можете изменять чужой договор");
            }
        }

        // 2. Найти услугу в списке услуг этого договора
        AdditionalService serviceToRemove = contract.getServices().stream()
                .filter(service -> service.getServiceId().equals(serviceId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Услуга не найдена в этом договоре"));

        // 3. Удалить услугу из коллекции
        contract.getServices().remove(serviceToRemove);

        // 4. Обновить сумму платежа в договоре
        recalculateAndSetMonthlyFee(contract);

         contractRepository.save(contract); // для читаемости
    }

    @Override
    @Transactional
    public void addServiceToContract(Long userId, Long contractId, Long serviceId) {

        // 1. Найти договор
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Договор не найден"));

        if (!contract.getSubscriber().getId().equals(userId)) {
            throw new AccessDeniedException("Вы не можете изменять чужой договор");
        }

        // 3. ПРОВЕРКА НА ДУБЛИКАТ: Проверить, не подключена ли уже услуга
        boolean isServiceAlreadyConnected = contract.getServices().stream()
                .anyMatch(service -> service.getServiceId().equals(serviceId));

        if (isServiceAlreadyConnected) {
            // Используем более подходящий тип исключения
            throw new IllegalArgumentException("Эта услуга уже подключена к данному договору.");
        }

        AdditionalService serviceToAdd = additionalServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Услуга с ID " + serviceId + " не найдена"));

        // 5. Добавить услугу в коллекцию.
        contract.getServices().add(serviceToAdd);

        // 6. Обновить сумму платежа в договоре
        recalculateAndSetMonthlyFee(contract);

        contractRepository.save(contract);
    }

    @Override
    @Transactional(readOnly = true)
    public ChangeTariffPreviewDto getTariffChangePreview(Long userId, Long contractId, Long newTariffId) {
        // 1. Найти договор и проверить права
        Contract contract = contractRepository.findByIdAndSubscriberId(contractId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Договор не найден или не принадлежит вам"));

        // 2. Найти новый тариф
        Tariff newTariff = tariffRepository.findById(newTariffId)
                .orElseThrow(() -> new ResourceNotFoundException("Новый тариф не найден"));

        // 3. Получить множество ID услуг, доступных в НОВОМ тарифе
        Set<Long> availableInNewTariffIds = newTariff.getAvailableServices().stream()
                .map(AdditionalService::getServiceId)
                .collect(Collectors.toSet());

        // 4. Найти услуги, которые ПОДКЛЮЧЕНЫ сейчас, но НЕДОСТУПНЫ в новом тарифе
        List<AdditionalService> servicesToDisconnect = contract.getServices().stream()
                .filter(connectedService -> !availableInNewTariffIds.contains(connectedService.getServiceId()))
                .collect(Collectors.toList());

        // 5. Посчитать новую стоимость (опционально, но полезно)
        recalculateAndSetMonthlyFee(contract);
        var newMonthlyFee = contract.getMonthlyFee();

        ChangeTariffPreviewDto previewDto = new ChangeTariffPreviewDto();
        previewDto.setServicesToDisconnect(AdditionalServiceMapper.toDtoList(servicesToDisconnect));
        previewDto.setNewMonthlyFee(newMonthlyFee);

        return previewDto;    }

    @Override
    @Transactional
    public ContractWithServicesDto changeTariff(Long userId, Long contractId, Long newTariffId) {
        Contract contract = contractRepository.findContractByIdWithServices(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Контракт не найден."));

        log.info("Контракт с ID {} действительно есть, сейчас будут проверки", contractId);


        if (!contract.getSubscriber().getId().equals(userId)) {
            throw new AccessDeniedException("Вы не можете изменять чужой договор");
        }

        if (contract.getTariff().getId().equals(newTariffId)) {
            throw new IllegalArgumentException("Вы уже используете этот тариф.");
        }

        log.info("Изменяю тариф на {}", newTariffId);

        Tariff newTariff = tariffRepository.findByIdWithAvailableServices(newTariffId)
                .orElseThrow(() -> new ResourceNotFoundException("Новый тариф не найден."));

        Set<Long> availableInNewTariffIds = newTariff.getAvailableServices().stream()
                .map(AdditionalService::getServiceId)
                .collect(Collectors.toSet());

        // Фильтр текущих услуг в договоре
        List<AdditionalService> remainingServices = contract.getServices().stream()
                .filter(connectedService -> availableInNewTariffIds.contains(connectedService.getServiceId()))
                .collect(Collectors.toList());

        contract.setTariff(newTariff);
        contract.setServices(remainingServices);

        recalculateAndSetMonthlyFee(contract);

        return toContractWithServicesDto(contract);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractWithServicesDto> getContractsWithServicesForUser(Long userId) {
        List<Contract> contracts = contractRepository.findAllBySubscriberIdWithServices(userId);

        // Маппим результат в DTO
        return contracts.stream()
                .map(this::toContractWithServicesDto)
                .collect(Collectors.toList());
    }

    private ContractWithServicesDto toContractWithServicesDto(Contract contract) {
        ContractWithServicesDto dto = new ContractWithServicesDto();

        var tariffId = contract.getTariff().getId();
        dto.setId(contract.getId());
        dto.setContractNumber(contract.getContractNumber());
        dto.setTariffId(contract.getTariff().getId());
        dto.setTariffName(tariffRepository.
                findById(tariffId).orElseThrow(() -> new ResourceNotFoundException("Тариф с ID" + tariffId +" Не найден."))
                .getName()
        );
        dto.setTariffMonthlyFee(contract.getTariff().getInstallationFee());
        dto.setServiceAddress(contract.getServiceAddress());
        dto.setSigningDate(contract.getSigningDate());
        dto.setServiceStartDate(contract.getServiceStartDate());
        dto.setMonthlyFee(contract.getMonthlyFee());
        dto.setTotalFee(contract.getMonthlyFee());
        dto.setServices(AdditionalServiceMapper.toDtoList(contract.getServices()));

        return dto;
    }

    private ContractResponseDto toResponseDto(Contract contract) {
        ContractResponseDto dto = new ContractResponseDto();

        dto.setId(contract.getId());
        dto.setContractNumber(contract.getContractNumber());
        dto.setServiceAddress(contract.getServiceAddress());

        dto.setSubscriberId(contract.getSubscriber().getId());

        dto.setTariffId(contract.getTariff().getId());
        dto.setSigningDate(contract.getSigningDate());
        dto.setServiceStartDate(contract.getServiceStartDate());
        dto.setMonthlyFee(contract.getMonthlyFee());

        return dto;
    }

    private Contract toEntity(ContractCreateDto createDto) {
        Contract contract = new Contract();

        // "ссылки" сущностей
        Subscriber subscriberReference = (Subscriber) userRepository.getReferenceById(createDto.getSubscriberId());
        Tariff tariffReference = tariffRepository.getReferenceById(createDto.getTariffId());

        contract.setContractNumber(createDto.getContractNumber());
        contract.setServiceAddress(createDto.getServiceAddress());
        contract.setSigningDate(createDto.getSigningDate());
        contract.setServiceStartDate(createDto.getServiceStartDate());
        contract.setMonthlyFee(createDto.getMonthlyFee());

        // Использование "ссылки" вместо полноценного объекта
        contract.setSubscriber(subscriberReference);
        contract.getTariff().setId(tariffReference.getId());

        return contract;
    }
}
