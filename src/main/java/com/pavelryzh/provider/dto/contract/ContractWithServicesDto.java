package com.pavelryzh.provider.dto.contract;

import com.pavelryzh.provider.dto.service.AdditionalServiceResponseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ContractWithServicesDto {
    private Long id;
    private String contractNumber;
    private String serviceAddress;
    private Long tariffId;
    private String tariffName;
    private LocalDate signingDate;
    private LocalDate serviceStartDate;
    private BigDecimal monthlyFee;
    private BigDecimal tariffMonthlyFee;
    private List<AdditionalServiceResponseDto> services;
    private BigDecimal totalFee;
}