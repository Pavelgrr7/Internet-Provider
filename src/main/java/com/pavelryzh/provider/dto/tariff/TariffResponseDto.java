package com.pavelryzh.provider.dto.tariff;


import com.pavelryzh.provider.dto.service.AdditionalServiceResponseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class TariffResponseDto {

    private Long id; // Мы отдаем клиенту ID, который он сможет использовать для будущих запросов (обновление, удаление)
    private String name;
    private String declaredSpeed;
    private BigDecimal installationFee;
    private String ipAddressType;
    private LocalDate startDate;
    private List<AdditionalServiceResponseDto> availableServices;
}
