package com.pavelryzh.provider.service;

import com.pavelryzh.provider.dto.report.ReportCreateDto;
import com.pavelryzh.provider.dto.report.ReportResponseDto;
import com.pavelryzh.provider.dto.tariff.TariffResponseDto;
import com.pavelryzh.provider.model.ReportId;
import jakarta.validation.Valid;

import java.util.List;

public interface ReportService {
    List<ReportResponseDto> findAllReports();
    ReportResponseDto findReport(Integer year, Long tariffId);

    ReportResponseDto createReport(ReportCreateDto createDto);

    void deleteReportById(ReportId reportId);

    ReportResponseDto recalculateReport(ReportId reportId);
}
