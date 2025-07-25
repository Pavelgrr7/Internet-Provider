package com.pavelryzh.provider.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
public class ReportResponseDto {

    @NonNull
    private String reportYear;

    @NonNull
    private BigDecimal tariffName;

    @NonNull
    private BigInteger subscriberCount;

    @NonNull
    private Double totalPayments;

    private Integer minDurationDays;

    private Integer maxDurationDays;
}
