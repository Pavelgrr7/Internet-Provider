package com.pavelryzh.provider.service;

import com.pavelryzh.provider.dto.contract.ContractCreateDto;
import com.pavelryzh.provider.dto.contract.ContractResponseDto;
import com.pavelryzh.provider.dto.contract.ContractWithServicesDto;
import com.pavelryzh.provider.dto.tariff.ChangeTariffPreviewDto;
import com.pavelryzh.provider.dto.user.subscriber.ContractInfo;

import java.util.List;
import java.util.Map;

public interface ContractService {

    ContractResponseDto create(ContractCreateDto contractCreateDto);

    List<ContractResponseDto> getAll();
    List<ContractResponseDto> getContractsByUserId(long id);
    List<ContractWithServicesDto> getContractsWithServicesForUser(Long userId);
    ContractResponseDto getContractDtoById(Long contractId);
    Map<Long, List<ContractInfo>> getContractInfoForUserIds(List<Long> subscriberIds);
    ChangeTariffPreviewDto getTariffChangePreview(Long id, Long contractId, Long newTariffId);

    void addServiceToContract(Long userId, Long contractId, Long serviceId);

    ContractWithServicesDto changeTariff(Long userId, Long contractId, Long tariffId);
    void changeAddress(Long id, Long contractId, String newAddress);
    void changeStartDate(Long id, Long contractId, String newStartDate);

    void removeServiceFromContract(Long userId, Long contractId, Long serviceId);
}
