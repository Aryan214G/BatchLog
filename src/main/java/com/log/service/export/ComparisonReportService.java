package com.log.service.export;

import com.log.dto.ComparisonReportData;

public class ComparisonReportService
        implements ReportGenerator<ComparisonReportData> {

    @Override
    public void generateReport(
            ComparisonReportData data
    ) throws Exception {

        // PDF generation here
    }
}