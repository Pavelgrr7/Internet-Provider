package com.pavelryzh.provider.dto.contract;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractSummaryDto {
    private Long id;
    private String contractNumber;
    private String serviceAddress;
    private LocalDate signingDate;
}
