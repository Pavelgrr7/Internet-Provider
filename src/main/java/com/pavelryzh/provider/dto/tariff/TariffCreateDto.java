package com.pavelryzh.provider.dto.tariff;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class TariffCreateDto {

    @NotBlank(message = "Название тарифа не может быть пустым")
    @Size(max = 100, message = "Название тарифа не может превышать 100 символов")
    private String name;

    @NotBlank(message = "Необходимо указать заявленную скорость")
    @Size(max = 50)
    private String declaredSpeed;

    @NotNull(message = "Стоимость установки не может быть null")
    @PositiveOrZero(message = "Стоимость установки должна быть 0 или больше")
    private BigDecimal installationFee;

    // todo использовать Enum
    @NotBlank
    private String ipAddressType;

    @NotNull(message = "Дата начала действия тарифа обязательна")
    private LocalDate startDate;

    private List<Long> serviceIds;
}
