package com.pavelryzh.provider.service;

import com.pavelryzh.provider.dto.tariff.TariffCreateDto;
import com.pavelryzh.provider.dto.tariff.TariffResponseDto;
import com.pavelryzh.provider.model.Tariff;
import com.pavelryzh.provider.repository.ContractRepository;
import com.pavelryzh.provider.repository.ServiceRepository;
import com.pavelryzh.provider.repository.TariffRepository;
import com.pavelryzh.provider.service.impl.TariffServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TariffServiceImplTest {

    @Mock
    private TariffRepository tariffRepository;
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private TariffServiceImpl tariffService;

    @Test
    void create_ShouldSuccessfullyCreateTariff_WhenDataIsValid() {

        TariffCreateDto createDto = new TariffCreateDto();
        createDto.setName("Новый Тариф");
        createDto.setDeclaredSpeed("100 Мбит/с");
        createDto.setInstallationFee(new BigDecimal("500"));
        createDto.setIpAddressType("Динамический");
        createDto.setStartDate(LocalDate.now());

        when(tariffRepository.existsByName(createDto.getName())).thenReturn(false);

        Tariff savedTariff = new Tariff();
        savedTariff.setId(10L);
        savedTariff.setName(createDto.getName());
        savedTariff.setDeclaredSpeed(createDto.getDeclaredSpeed());
        savedTariff.setInstallationFee(createDto.getInstallationFee());
        savedTariff.setIpAddressType(createDto.getIpAddressType());
        savedTariff.setStartDate(createDto.getStartDate());
        savedTariff.setAvailableServices(Collections.emptyList());

        // Объект savedTariff с проставленным id
        when(tariffRepository.save(any(Tariff.class))).thenReturn(savedTariff);

        // Тест метода create
        TariffResponseDto response = tariffService.create(createDto);

        // Проверки
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Новый Тариф", response.getName());
        // Проверка записи в БД
        verify(tariffRepository, times(1)).save(any(Tariff.class));
    }

    @Test
    void create_ShouldThrowException_WhenTariffNameAlreadyExists() {
        TariffCreateDto createDto = new TariffCreateDto();
        createDto.setName("Существующий Тариф");

        // Сценарий - в БД уже есть такая запись
        when(tariffRepository.existsByName(createDto.getName())).thenReturn(true);
        // Проверка, что код действительно падает с нужной ошибкой
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tariffService.create(createDto));

        assertEquals("Тариф с названием 'Существующий Тариф' уже существует.", exception.getMessage());

        // Проверка записи в БД (не должно быть)
        verify(tariffRepository, never()).save(any(Tariff.class));
    }
}