package com.log.model;

public class BatchTest {
    private int testId;
    private int batchCode;
    private String testDate;
    private String testSite;
    private String SOP;

    public BatchTest(int testId, int batchCode, String testDate, String testSite, String sop)
    {
        this.testId = testId;
        this.batchCode = batchCode;
        this.testDate = testDate;
        this.testSite = testSite;
        this.SOP = sop;
    }

    public BatchTest(int batchCode, String testDate, String testSite, String SOP)
    {
        this.batchCode = batchCode;
        this.testDate = testDate;
        this.testSite = testSite;
        this.SOP = SOP;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(int batchCode) {
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
