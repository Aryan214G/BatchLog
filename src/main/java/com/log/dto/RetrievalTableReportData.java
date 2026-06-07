package com.log.dto;

import java.util.List;
import java.util.Map;

public class RetrievalTableReportData {

    private String projectName;
    private String sop;
    private String product;
    private String componentId;
    private String batchNo;
    private String testSchedule;

    private List<String> headers;
     private Map<String, List<List<String>>> groupedRows;


    public RetrievalTableReportData(List<String> headers, Map<String, List<List<String>>> groupedRows, String projectName, String sop, String product, String componentId, String batchNo, String testSchedule) {
        this.headers = headers;
        this.groupedRows = groupedRows;
        this.projectName = projectName;
        this.sop = sop;
        this.product = product;
        this.componentId = componentId;
        this.batchNo = batchNo;
        this.testSchedule = testSchedule;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSop() {
        return sop;
    }

    public void setSop(String sop) {
        this.sop = sop;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getTestSchedule() {
        return testSchedule;
    }

    public void setTestSchedule(String testSchedule) {
        this.testSchedule = testSchedule;
    }
}
