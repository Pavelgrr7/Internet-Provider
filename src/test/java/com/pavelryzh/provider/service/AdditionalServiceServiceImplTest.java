package com.pavelryzh.provider.service;

import com.pavelryzh.provider.dto.service.AdditionalServiceResponseDto;
import com.pavelryzh.provider.model.AdditionalService;
import com.pavelryzh.provider.model.Tariff;
import com.pavelryzh.provider.repository.ServiceRepository;
import com.pavelryzh.provider.repository.TariffRepository;
import com.pavelryzh.provider.service.impl.AdditionalServiceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdditionalServiceServiceImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private TariffRepository tariffRepository;

    @InjectMocks
    private AdditionalServiceServiceImpl additionalServiceService;

    @Test
    void create_ShouldReturnDto_WhenTariffExists() {
        Long tariffId = 1L;
        String serviceName = "Антивирус";
        BigDecimal serviceCost = new BigDecimal("150.00");

        Tariff mockTariff = new Tariff();
        mockTariff.setId(tariffId);

        // Поведение мок-объкта

        when(tariffRepository.findById(tariffId)).thenReturn(Optional.of(mockTariff));
        when(serviceRepository.save(any(AdditionalService.class))).thenAnswer(invocation -> {
            AdditionalService savedService = invocation.getArgument(0);
            savedService.setServiceId(99L);
            return savedService;
        });

        // Тест метода create
        AdditionalServiceResponseDto result = additionalServiceService.create(tariffId, serviceName, serviceCost);

        // Проверка результата
        assertNotNull(result);
        assertEquals(serviceName, result.getServiceName());
        assertEquals(serviceCost, result.getCost());

        // Проверка записи в БД
        verify(serviceRepository, times(1)).save(any(AdditionalService.class));
    }
}