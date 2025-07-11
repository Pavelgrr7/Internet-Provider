package com.pavelryzh.provider.service;


import com.pavelryzh.provider.dto.tariff.TariffCreateDto;
import com.pavelryzh.provider.dto.tariff.TariffResponseDto;
import com.pavelryzh.provider.dto.tariff.TariffUpdateDto;
import jakarta.validation.Valid;

import java.util.List;

public interface TariffService {
    TariffResponseDto create(TariffCreateDto createDto);
    TariffResponseDto getById(Long id);
    List<TariffResponseDto> getAll();
    TariffResponseDto update(Long id, @Valid TariffUpdateDto updateDto);
}