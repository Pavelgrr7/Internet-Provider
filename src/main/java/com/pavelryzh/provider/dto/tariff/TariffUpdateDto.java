package com.pavelryzh.provider.dto.tariff;


import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
    public class TariffUpdateDto {

    @Size(max = 100, message = "Название тарифа не может превышать 100 символов")
    private String name;

    @Size(max = 50)
    private String declaredSpeed;

    @PositiveOrZero(message = "Стоимость установки должна быть 0 или больше")
    private BigDecimal installationFee;

    private String ipAddressType;

    private LocalDate startDate;
}
