package com.log.dto;

import java.util.List;
import java.util.Map;

public class ComparisonReportData {

    private String title;

    // Batch No. / Component ID headers
    private List<String> columnHeaders;

    // Property -> values
    private Map<String, List<String>> rows;

    public ComparisonReportData(
            String title,
            List<String> columnHeaders,
            Map<String, List<String>> rows
    ) {
        this.title = title;
        this.columnHeaders = columnHeaders;
        this.rows = rows;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getColumnHeaders() {
        return columnHeaders;
    }

    public Map<String, List<String>> getRows() {
        return rows;
    }
}
