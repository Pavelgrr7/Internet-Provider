package com.pavelryzh.provider.dto.tariff;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TariffCreateDto {

    @NotBlank(message = "Название тарифа не может быть пустым")
    @Size(max = 100, message = "Название тарифа не может превышать 100 символов")
    private String name; // Упрощенное имя поля

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
    @FutureOrPresent(message = "Дата начала не может быть в прошлом")
    private LocalDate startDate;
}
