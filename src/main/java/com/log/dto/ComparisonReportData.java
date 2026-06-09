package com.log.dto;

import java.util.List;
import java.util.Map;

public class ComparisonReportData {

    private List<String> columnHeaders;

    // Property -> averages
    private Map<String, List<String>> rows;

    public ComparisonReportData(
            List<String> columnHeaders,
            Map<String, List<String>> rows
    ) {
        this.columnHeaders = columnHeaders;
        this.rows = rows;
    }

    public List<String> getColumnHeaders() {
        return columnHeaders;
    }

    public Map<String, List<String>> getRows() {
        return rows;
    }
}
