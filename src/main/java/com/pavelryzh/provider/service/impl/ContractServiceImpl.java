package com.pavelryzh.provider.service.impl;

import com.pavelryzh.provider.dto.contract.ContractCreateDto;
import com.pavelryzh.provider.dto.contract.ContractResponseDto;
import com.pavelryzh.provider.dto.tariff.TariffCreateDto;
import com.pavelryzh.provider.dto.tariff.TariffResponseDto;
import com.pavelryzh.provider.dto.user.subscriber.ContractInfo;
import com.pavelryzh.provider.exception.ResourceNotFoundException;
import com.pavelryzh.provider.model.Contract;
import com.pavelryzh.provider.model.Tariff;
import com.pavelryzh.provider.repository.ContractRepository;
import com.pavelryzh.provider.service.ContractService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {

    private ContractRepository contractRepository;

    public ContractServiceImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
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
        return contractRepository.findBySubscriberId(id).stream()
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
            List<Contract> contracts = contractRepository.findBySubscriberId(subscriberId);
            contracts.forEach(
                    contract -> {
                        infoList.add(
                                new ContractInfo(
                                        contract.getId(),
                                        contract.getContractNumber()
                                )
                        );
                    }
            );
            contractsInfoMap.put(subscriberId, infoList);
        }

        return contractsInfoMap;
    }

    private ContractResponseDto toResponseDto(Contract contract) {
        ContractResponseDto dto = new ContractResponseDto();

        dto.setId(contract.getId());
        dto.setContractNumber(contract.getContractNumber());
        dto.setServiceAddress(contract.getServiceAddress());
        dto.setSubscriberId(contract.getSubscriberId());
        dto.setSigningDate(contract.getSigningDate());
        dto.setServiceStartDate(contract.getServiceStartDate());
        dto.setMonthlyFee(contract.getMonthlyFee());

        return dto;
    }

    private Contract toEntity(ContractCreateDto createDto) {
        Contract contract = new Contract();
        contract.setContractNumber(createDto.getContractNumber());
        contract.setSubscriberId(createDto.getSubscriberId());
        contract.setTariffId(createDto.getTariffId());
        contract.setServiceAddress(createDto.getServiceAddress());
        contract.setSigningDate(createDto.getSigningDate());
        contract.setServiceStartDate(createDto.getServiceStartDate());
        contract.setMonthlyFee(createDto.getMonthlyFee());
        return contract;
    }
}
