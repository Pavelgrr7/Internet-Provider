package com.pavelryzh.provider.dto.contract;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
//@AllArgsConstructor
public class ContractResponseDto {

    @Id
    private long id;

    private String contractNumber;

    private long subscriberId;

    private long tariffId;

    @NotNull
    private String serviceAddress;

    private LocalDate signingDate;

    private LocalDate serviceStartDate;

    private BigDecimal monthlyFee;

}
