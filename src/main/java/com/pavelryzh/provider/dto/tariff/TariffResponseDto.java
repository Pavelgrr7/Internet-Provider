package com.pavelryzh.provider.dto.tariff;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TariffResponseDto {

    private Long id; // Мы отдаем клиенту ID, который он сможет использовать для будущих запросов (обновление, удаление)
    private String name;
    private String declaredSpeed;
    private BigDecimal installationFee;
    private String ipAddressType;
    private LocalDate startDate;
    // todo: "активен ли тариф сейчас",
}
