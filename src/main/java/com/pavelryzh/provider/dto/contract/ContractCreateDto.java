package com.pavelryzh.provider.dto.contract;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ContractCreateDto {

    private Long subscriberId;

    @NotNull(message = "Необходимо выбрать тариф")
    private Long tariffId;

    @NotBlank(message = "Адрес предоставления услуги обязателен")
    private String serviceAddress;

    @NotNull(message = "Дата подписания договора обязательна")
    private LocalDate signingDate;

    @NotNull(message = "Дата начала предоставления услуг обязательна")
    private LocalDate serviceStartDate;

    private List<Long> serviceIds;
}
