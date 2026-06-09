package com.log.model;

public class BatchRow {

        private Integer batchCode;
        private String batchId;
        private String productName;
        private String testDate;
        private String testSite;
        private int testId;
        private String sop;
        private String componentId;
        private Integer productCode;
        private String testSchedule;



    public BatchRow(Integer batchCode, String batchId, String productName, String testDate, String testSite,Integer productCode,String sop,String testSchedule) {
        this.batchCode = batchCode;
        this.batchId = batchId;
        this.productName = productName;
        this.testDate = testDate;
        this.testSite = testSite;
        this.productCode= productCode;
        this.sop= sop;
        this.testSchedule=testSchedule;
    }
    public BatchRow(Integer batchCode, String batchId, String productName, String testDate, String testSite, int testId,Integer productCode,String sop,String testSchedule) {
        this.batchCode = batchCode;
        this.batchId = batchId;
        this.productName = productName;
        this.testDate = testDate;
        this.testSite = testSite;
        this.testId = testId;
        this.productCode=productCode;
        this.sop=sop;
        this.testSchedule=testSchedule;
    }

    public BatchRow(Integer batchCode, String batchId, String productName, String testDate, String testSite, Integer productCode, String componentId,String sop,String testSchedule) {
        this.batchCode = batchCode;
        this.batchId = batchId;
        this.productName = productName;
        this.testDate = testDate;
        this.testSite = testSite;
        this.productCode = productCode;
        this.componentId = componentId;
        this.sop=sop;
        this.testSchedule=testSchedule;
    }

    public BatchRow(Integer batchCode, String batchId, String productName, String testDate, String testSite, int testId, Integer productCode, String componentId,String sop,String testSchedule) {
        this.batchCode = batchCode;
        this.batchId = batchId;
        this.productName = productName;
        this.testDate = testDate;
        this.testSite = testSite;
        this.testId = testId;
        this.productCode = productCode;
        this.componentId = componentId;
        this.sop=sop;
        this.testSchedule=testSchedule;
    }

    public Integer getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(Integer batchCode) {
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

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public Integer getProductCode() {
        return productCode;
    }

    public void setProductCode(Integer productCode) {
        this.productCode = productCode;
    }

    public String getComponentId(){return this.componentId;}

    public void setComponentId(String Cid){this.componentId = Cid;}

    public String getSop() {
        return sop;
    }

    public void setSop(String sop) {
        this.sop = sop;
    }

    public void setTestSchedule(String testSchedule) {this.testSchedule = testSchedule;}

    public String getTestSchedule() {return testSchedule;}

}
