package com.pavelryzh.provider.service;


import com.pavelryzh.provider.dto.tariff.TariffCreateDto;
import com.pavelryzh.provider.dto.tariff.TariffResponseDto;

import java.util.List;

public interface TariffService {
    TariffResponseDto create(TariffCreateDto createDto);
    TariffResponseDto getById(Long id);
    List<TariffResponseDto> getAll();
}