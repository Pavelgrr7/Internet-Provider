package com.pavelryzh.provider.service.impl;

import com.pavelryzh.provider.dto.mapper.AdditionalServiceMapper;
import com.pavelryzh.provider.dto.service.AdditionalServiceResponseDto;
import com.pavelryzh.provider.model.AdditionalService;
import com.pavelryzh.provider.model.Tariff;
import com.pavelryzh.provider.repository.ServiceRepository;
import com.pavelryzh.provider.repository.TariffRepository;
import com.pavelryzh.provider.service.AdditionalServiceService;
import com.pavelryzh.provider.service.TariffService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdditionalServiceServiceImpl implements AdditionalServiceService {

    private final TariffRepository tariffRepository;
    ServiceRepository serviceRepository;

    public AdditionalServiceServiceImpl(ServiceRepository serviceRepository, TariffRepository tariffRepository) {
        this.serviceRepository = serviceRepository;
        this.tariffRepository = tariffRepository;
    }

    @Override
    @Transactional
    public AdditionalServiceResponseDto create(Long boundedTariffId, String serviceName, BigDecimal serviceCost) {

        Tariff boundedTariff = tariffRepository.findById(boundedTariffId).orElseThrow();

        AdditionalService newService = new AdditionalService();
        newService.setServiceName(serviceName);
        newService.setCost(serviceCost);
        newService.setTariffs(List.of(boundedTariff));

        serviceRepository.save(newService);

        return AdditionalServiceMapper.toDto(newService);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdditionalServiceResponseDto> getAll() {
        return AdditionalServiceMapper.toDtoList(serviceRepository.findAll());
    }
}
