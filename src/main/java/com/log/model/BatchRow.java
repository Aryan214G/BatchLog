package com.log.model;

public class BatchRow {

        private int batchCode;
        private String batchId;
        private String productName;
        private String testDate;
        private String testSite;

    public BatchRow(int batchCode, String batchId, String productName, String testDate, String testSite) {
        this.batchCode = batchCode;
        this.batchId = batchId;
        this.productName = productName;
        this.testDate = testDate;
        this.testSite = testSite;
    }

    public int getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(int batchCode) {
        this.batchCode = batchCode;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getTestSite() {
        return testSite;
    }

    public void setTestSite(String testSite) {
        this.testSite = testSite;
    }
}
