package com.pavelryzh.provider.dto.report;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReportCreateDto {
    @NotNull(message = "Год отчета не может быть пустым")
    @Min(value = 1991, message = "Год отчета должен быть после 1990")
    private Integer reportYear;

    @NotNull(message = "ID тарифа не может быть пустым")
    private Long tariffId;

}
