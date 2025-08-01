package com.pavelryzh.provider.service;


import com.pavelryzh.provider.dto.service.AdditionalServiceResponseDto;
import com.pavelryzh.provider.dto.tariff.TariffCreateDto;
import com.pavelryzh.provider.dto.tariff.TariffResponseDto;
import com.pavelryzh.provider.dto.tariff.TariffSelectionDto;
import com.pavelryzh.provider.dto.tariff.TariffUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public interface TariffService {
    TariffResponseDto create(TariffCreateDto createDto);
    TariffResponseDto getById(Long id);
    List<TariffResponseDto> getAll();
    TariffResponseDto update(Long id, @Valid TariffUpdateDto updateDto);
    void remove(Long id);
    List<AdditionalServiceResponseDto> getAvailableServicesByTariffId(Long tariffId);
    List<AdditionalServiceResponseDto> getAvailableServicesForContract(Long contractId);

    List<TariffSelectionDto> findActiveTariffs(Integer year);

    List<Integer> findActiveYearsForTariff(Long tariffId);

    List<TariffSelectionDto> findTariffsForSelection();

    void addServiceToTariff(Long tariffId, @NotNull Long serviceId);

    void removeServiceFromTariff(Long tariffId, Long serviceId);
}