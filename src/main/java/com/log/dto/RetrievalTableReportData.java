package com.log.dto;

import java.util.List;
import java.util.Map;

public class RetrievalTableReportData {

    private List<String> headers;
     private Map<String, List<List<String>>> groupedRows;

    public RetrievalTableReportData(List<String> headers, Map<String, List<List<String>>> groupedRows) {
        this.headers = headers;
        this.groupedRows = groupedRows;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public Map<String, List<List<String>>> getGroupedRows() {
        return groupedRows;
    }

    public void setGroupedRows(Map<String, List<List<String>>> groupedRows) {
        this.groupedRows = groupedRows;
    }
}
