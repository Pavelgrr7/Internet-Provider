package com.pavelryzh.provider.dto.tariff;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeTariffRequestDto {
    @NotNull(message = "ID нового тарифа не может быть пустым")
    private Long tariffId;
}
