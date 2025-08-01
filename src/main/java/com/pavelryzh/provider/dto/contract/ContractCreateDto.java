package com.pavelryzh.provider.dto.contract;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ContractCreateDto {

    @NotNull
    private String contractNumber;

    @NotNull
    private Long subscriberId;

    @NotNull
    private Long tariffId;

    @NotNull(message = "Адрес предоставления услуги обязателен")
    private String serviceAddress;

    @NotNull(message = "Дата подписания договора обязательна")
    private LocalDate signingDate;

    @NotNull(message = "Дата начала предоставления услуг обязательна")
    private LocalDate serviceStartDate;

    @NotNull(message = "Абонентская плата не может быть null")
    @PositiveOrZero(message = "Абонентская плата должна быть 0 или больше")
    private BigDecimal monthlyFee;
}
