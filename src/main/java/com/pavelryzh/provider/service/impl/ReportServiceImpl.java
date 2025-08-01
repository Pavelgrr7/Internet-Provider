package com.pavelryzh.provider.service.impl;

import com.pavelryzh.provider.dto.report.ReportCreateDto;
import com.pavelryzh.provider.dto.report.ReportResponseDto;
import com.pavelryzh.provider.exception.ResourceNotFoundException;
import com.pavelryzh.provider.model.Contract;
import com.pavelryzh.provider.model.Report;
import com.pavelryzh.provider.model.ReportId;
import com.pavelryzh.provider.model.Tariff;
import com.pavelryzh.provider.repository.ContractRepository;
import com.pavelryzh.provider.repository.ReportRepository;
import com.pavelryzh.provider.repository.TariffRepository;
import com.pavelryzh.provider.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final TariffRepository tariffRepository;
    private final ContractRepository contractRepository; // Нужен для вычисления данных


    public ReportServiceImpl(ReportRepository reportRepository, TariffRepository tariffRepository, ContractRepository contractRepository) {
        this.reportRepository = reportRepository;
        this.tariffRepository = tariffRepository;
        this.contractRepository = contractRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportResponseDto> findAllReports() {
        var reports = reportRepository.findAll();
        return reports.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReportResponseDto findReport(Integer year, Long tariffId) {
        ReportId reportId = new ReportId(year, tariffId);
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Отчет не найден"));
        return toResponseDto(report);
    }

    @Transactional
    public ReportResponseDto createReport(ReportCreateDto createDto) {
        // --- 1. ПРОВЕРКИ ---

        // Находим тариф, чтобы убедиться, что он существует
        Tariff tariff = tariffRepository.findById(createDto.getTariffId())
                .orElseThrow(() -> new ResourceNotFoundException("Тариф с ID " + createDto.getTariffId() + " не найден."));

        // Создаем ID для нового отчета
        ReportId reportId = new ReportId(createDto.getReportYear(), createDto.getTariffId());

        // Проверяем, не существует ли уже такой отчет
        if (reportRepository.existsById(reportId)) {
            throw new IllegalArgumentException("Отчет для этого тарифа за этот год уже существует.");
        }

        // --- 2. БИЗНЕС-ЛОГИКА: ВЫЧИСЛЕНИЕ ДАННЫХ ДЛЯ ОТЧЕТА ---

        // Находим все договоры по данному тарифу, которые были активны в указанном году.
        // Для этого понадобится специальный метод в ContractRepository.
        List<Contract> relevantContracts =
                contractRepository.findActiveContractsByTariffAndYear(createDto.getTariffId(), createDto.getReportYear());

        if (relevantContracts.isEmpty()) {
            throw new ResourceNotFoundException("Не найдено активных договоров для данного тарифа в указанном году.");
        }

        long subscriberCount = relevantContracts.size();

        // Считаем общую сумму платежей (упрощенно: месячная плата * 12)
        BigDecimal totalPayments = relevantContracts.stream()
                .map(contract -> contract.getMonthlyFee().multiply(new BigDecimal("12")))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Считаем мин/макс длительность (упрощенно)
        long minDuration = relevantContracts.stream()
                .mapToLong(c -> ChronoUnit.DAYS.between(c.getServiceStartDate(), c.getSigningDate().withDayOfYear(365)))
                .min().orElse(0);

        long maxDuration = relevantContracts.stream()
                .mapToLong(c -> ChronoUnit.DAYS.between(c.getServiceStartDate(), c.getSigningDate().withDayOfYear(365)))
                .max().orElse(0);

        Report report = new Report();
        report.setId(reportId);
        report.setTariff(tariff);
        report.setSubscriberCount(subscriberCount);
        report.setTotalPayments(totalPayments);
        report.setMinDurationDays((int) minDuration);
        report.setMaxDurationDays((int) maxDuration);

        Report savedReport = reportRepository.save(report);

        return toResponseDto(savedReport);
    }

    private ReportResponseDto toResponseDto(Report report) {
        ReportResponseDto dto = new ReportResponseDto(
                report.getId().getReportYear());
        dto.setTariffName(report.getTariff().getName());
        dto.setTotalPayments(report.getTotalPayments());
        dto.setSubscriberCount(report.getSubscriberCount());
        dto.setMinDurationDays(report.getMinDurationDays());
        dto.setMaxDurationDays(report.getMaxDurationDays());
        return dto;
    }
}
