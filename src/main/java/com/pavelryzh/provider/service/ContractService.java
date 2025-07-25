package com.pavelryzh.provider.service;

import com.pavelryzh.provider.dto.contract.ContractCreateDto;
import com.pavelryzh.provider.dto.contract.ContractResponseDto;
import com.pavelryzh.provider.dto.tariff.TariffResponseDto;

import java.util.List;

public interface ContractService {
    ContractResponseDto create(ContractCreateDto contractCreateDto);
    List<ContractResponseDto> getAll();
    List<ContractResponseDto> getContractsByUserId(long id);
}
