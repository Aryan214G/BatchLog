package com.log.model;

public class BatchTest {
    private int testId;
    private int batchCode;
    private String testDate;
    private String testSite;


    public void BatchTest(int batchCode, String testDate, String testSite) {
        this.batchCode = batchCode;
        this.testDate = testDate;
        this.testSite = testSite;
    }


    public void BatchTest(int testId, int batchCode, String testDate, String testSite) {
        this.testId = testId;
        this.batchCode = batchCode;
        this.testDate = testDate;
        this.testSite = testSite;
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
}
