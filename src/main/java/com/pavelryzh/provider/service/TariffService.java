package com.pavelryzh.provider.service;


import com.pavelryzh.provider.dto.tariff.TariffCreateDto;
import com.pavelryzh.provider.dto.tariff.TariffResponseDto;
import com.pavelryzh.provider.exception.ResourceNotFoundException;

public interface TariffService {
    TariffResponseDto create(TariffCreateDto createDto);
    TariffResponseDto getById(Long id) throws ResourceNotFoundException;
}