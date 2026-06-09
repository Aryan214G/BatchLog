package com.log.service.export;

import com.log.dto.ComparisonReportData;

public class ComparisonReportService
        implements Exporter<ComparisonReportData> {

    @Override
    public void export(
            ComparisonReportData data
    ) throws Exception {

        // PDF generation here
    }
}