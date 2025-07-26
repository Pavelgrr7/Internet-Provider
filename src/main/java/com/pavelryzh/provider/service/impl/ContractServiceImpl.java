package com.pavelryzh.provider.service.impl;

import com.pavelryzh.provider.dto.contract.ContractCreateDto;
import com.pavelryzh.provider.dto.contract.ContractResponseDto;
import com.pavelryzh.provider.dto.contract.ContractWithServicesDto;
import com.pavelryzh.provider.dto.mapper.AdditionalServiceMapper;
import com.pavelryzh.provider.dto.service.AdditionalServiceResponseDto;
import com.pavelryzh.provider.dto.user.subscriber.ContractInfo;
import com.pavelryzh.provider.exception.ResourceNotFoundException;
import com.pavelryzh.provider.model.AdditionalService;
import com.pavelryzh.provider.model.Contract;
import com.pavelryzh.provider.model.Subscriber;
import com.pavelryzh.provider.model.Tariff;
import com.pavelryzh.provider.repository.ContractRepository;
import com.pavelryzh.provider.repository.TariffRepository;
import com.pavelryzh.provider.repository.UserRepository;
import com.pavelryzh.provider.service.ContractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final UserRepository subscriberRepository;
    private final TariffRepository tariffRepository;

    public ContractServiceImpl(ContractRepository contractRepository, UserRepository subscriberRepository, TariffRepository tariffRepository) {
        this.contractRepository = contractRepository;
        this.subscriberRepository = subscriberRepository;
        this.tariffRepository = tariffRepository;
    }
    @Override
    @Transactional
    public ContractResponseDto create(ContractCreateDto contractCreateDto) {
        return null;
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

    @Override
    @Transactional(readOnly = true)
    public List<ContractWithServicesDto> getContractsWithServicesForUser(Long userId) {
        List<Contract> contracts = contractRepository.findAllBySubscriberIdWithServices(userId);

        // Маппим результат в наши DTO
        return contracts.stream()
                .map(this::toContractWithServicesDto)
                .collect(Collectors.toList());
    }

    private ContractWithServicesDto toContractWithServicesDto(Contract contract) {
        ContractWithServicesDto dto = new ContractWithServicesDto();

        var tariffId = contract.getTariffId();
        dto.setId(contract.getId());
        dto.setContractNumber(contract.getContractNumber());
        dto.setTariffId(contract.getTariffId());
        dto.setTariffName(tariffRepository.
                findById(tariffId).orElseThrow(() -> new ResourceNotFoundException("Тариф с ID" + tariffId +" Не найден."))
                .getName()
        );
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

        dto.setTariffId(contract.getTariffId());
        dto.setSigningDate(contract.getSigningDate());
        dto.setServiceStartDate(contract.getServiceStartDate());
        dto.setMonthlyFee(contract.getMonthlyFee());

        return dto;
    }

    private Contract toEntity(ContractCreateDto createDto) {
        Contract contract = new Contract();

        // Получаем "ссылки" на сущности, не загружая их полностью из БД.
        Subscriber subscriberReference = (Subscriber) subscriberRepository.getReferenceById(createDto.getSubscriberId());
        Tariff tariffReference = tariffRepository.getReferenceById(createDto.getTariffId());

        contract.setContractNumber(createDto.getContractNumber());
        contract.setServiceAddress(createDto.getServiceAddress());
        contract.setSigningDate(createDto.getSigningDate());
        contract.setServiceStartDate(createDto.getServiceStartDate());
        contract.setMonthlyFee(createDto.getMonthlyFee());

        // Использование "ссылки" вместо полноценного объекта
        contract.setSubscriber(subscriberReference);
        contract.setTariffId(tariffReference.getId());

        return contract;
    }
}
