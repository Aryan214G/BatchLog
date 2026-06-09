package com.log.model;

public class BatchTest {
    private int testId;
    private Integer batchCode;
    private String testDate;
    private String testSite;
    private String SOP;
    private Integer productCode;

    public BatchTest(int testId, Integer batchCode, String testDate, String testSite, Integer productCode,String SOP) {
        this.testId = testId;
        this.batchCode = batchCode;
        this.testDate = testDate;
        this.testSite = testSite;
        this.productCode = productCode;
        this.SOP = SOP;
    }

    public BatchTest(Integer batchCode, String testDate, String testSite,Integer productCode,String SOP) {
        this.batchCode = batchCode;
        this.testDate = testDate;
        this.testSite = testSite;
        this.productCode = productCode;
        this.SOP = SOP;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public Integer getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(Integer batchCode) {
        this.batchCode = batchCode;
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

    public String getSOP() {
        return SOP;
    }

    public void setSOP(String SOP) {
        this.SOP = SOP;
    }
    public Integer getProductCode() {
        return productCode;
    }

    public void setProductCode(Integer productCode) {
        this.productCode = productCode;
    }

    @Override
    public String toString() {
        return "BatchTest{" +
                "testId=" + testId +
                ", batchCode=" + batchCode +
                ", testDate='" + testDate + '\'' +
                ", testSite='" + testSite + '\'' +
                '}';
    }
}
