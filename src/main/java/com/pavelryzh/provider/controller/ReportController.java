package com.pavelryzh.provider.controller;

import com.pavelryzh.provider.dto.report.ReportCreateDto;
import com.pavelryzh.provider.dto.report.ReportResponseDto;
import com.pavelryzh.provider.model.ReportId;
import com.pavelryzh.provider.service.ReportService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('ADMIN')") // Вся работа с отчетами - только для админов
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<List<ReportResponseDto>> getAllReports() {
        log.info("getAllReports: {}", reportService.findAllReports());
        return ResponseEntity.ok(reportService.findAllReports());
    }

    @PostMapping
    public ResponseEntity<ReportResponseDto> createReport(@Valid @RequestBody ReportCreateDto createDto) {

        log.info("createReport: {}", createDto);

        ReportResponseDto createdReport = reportService.createReport(createDto);

        log.info("createdReport: {}", createdReport);

        // URI для созданного ресурса
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{year}/{tariffId}")
                .buildAndExpand(createdReport.getReportYear(), createdReport.getTariffId())
                .toUri();

        return ResponseEntity.created(location).body(createdReport);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/recalculate")
    public ResponseEntity<ReportResponseDto> recalculateReport(
            @RequestParam("year") Integer year,
            @RequestParam("tariffId") Long tariffId) {

        ReportId reportId = new ReportId(year, tariffId);

        ReportResponseDto updatedReport = reportService.recalculateReport(reportId);

        log.info("update report: {}", updatedReport);
        return ResponseEntity.ok(updatedReport);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Void> deleteReport(
            @RequestParam("year") Integer year,
            @RequestParam("tariffId") Long tariffId) {

        log.info("Запрос на удаление отчета за {} год для тарифа {}", year, tariffId);

        ReportId reportId = new ReportId(year, tariffId);
        reportService.deleteReportById(reportId);

        return ResponseEntity.noContent().build();
    }
}
