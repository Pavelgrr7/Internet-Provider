package com.pavelryzh.provider.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable // класс может быть встроен в другую сущность
public class ReportId implements Serializable {

    @Column(name = "report_year")
    private Integer reportYear;

    @Column(name = "tariff_id")
    private Long tariffId;
}
