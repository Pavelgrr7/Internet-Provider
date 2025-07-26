package com.pavelryzh.provider.repository;

import com.pavelryzh.provider.model.Report;
import com.pavelryzh.provider.model.ReportId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, ReportId> {
}
