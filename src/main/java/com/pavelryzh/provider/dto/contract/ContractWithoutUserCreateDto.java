package com.pavelryzh.provider.dto.contract;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ContractWithoutUserCreateDto {

    @NotNull
    private String contractNumber;

    @NotNull
    private Long tariffId;

    @NotNull(message = "Адрес предоставления услуги обязателен")
    private String serviceAddress;

    @NotNull(message = "Дата подписания договора обязательна")
    private LocalDate signingDate;

    @NotNull(message = "Дата начала предоставления услуг обязательна")
    private LocalDate serviceStartDate;

    private BigDecimal monthlyFee;
}

