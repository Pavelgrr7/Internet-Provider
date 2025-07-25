package com.pavelryzh.provider.dto.report;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ReportCreateDto {

    @NonNull
    private String reportYear;

    @NonNull
    private BigDecimal tariffName;

}
