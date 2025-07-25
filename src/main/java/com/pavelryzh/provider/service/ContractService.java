package com.pavelryzh.provider.service;

import com.pavelryzh.provider.dto.contract.ContractCreateDto;
import com.pavelryzh.provider.dto.contract.ContractResponseDto;
import com.pavelryzh.provider.dto.tariff.TariffResponseDto;
import com.pavelryzh.provider.dto.user.subscriber.ContractInfo;

import java.util.List;
import java.util.Map;

public interface ContractService {
    ContractResponseDto create(ContractCreateDto contractCreateDto);
    List<ContractResponseDto> getAll();
    List<ContractResponseDto> getContractsByUserId(long id);

    ContractResponseDto getContractDtoById(Long contractId);
    Map<Long, List<ContractInfo>> getContractInfoForUserIds(List<Long> subscriberIds);
}
