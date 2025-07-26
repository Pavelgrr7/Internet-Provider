package com.pavelryzh.provider.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name="reports")
public class Report {

    @EmbeddedId
    private ReportId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id", insertable = false, updatable = false) // Указываем имя колонки и запрещаем менять ее напрямую через эту связь
    private Tariff tariff;

    @Column(name = "subscriber_count")
    Long subscriberCount;

    @Column(name = "total_payments")
    BigDecimal totalPayments;

    @Column(name = "min_duration_days")
    Integer minDurationDays;

    @Column(name = "max_duration_days")
    Integer maxDurationDays;

}
