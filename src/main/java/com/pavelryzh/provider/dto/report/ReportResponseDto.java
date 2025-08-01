package com.pavelryzh.provider.dto.report;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class ReportResponseDto {

    @NonNull
    private Integer reportYear;

    private String tariffName;

    private Long tariffId;

    private Long subscriberCount;

    private BigDecimal totalPayments;

    private Integer minDurationDays;

    private Integer maxDurationDays;
}
