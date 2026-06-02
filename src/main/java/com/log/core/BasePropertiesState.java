package com.log.core;

import java.time.LocalDate;

public class BasePropertiesState {
    private static final BasePropertiesState instance = new BasePropertiesState();

    BasePropertiesState(){};

    public static BasePropertiesState getInstance() {
        return instance;
    }
    private int projectId;
    private String projectName;
    private String batchNo;
    private int batchCode;
    private String productName;
    private String productID;
    private int productCode;
    private LocalDate testDate;
    private String placeOfTesting;
    private String fileName;
    private int testId;
    private String sop;

    public int getTestId() {
        System.out.println(
                "BasePropertiesState.getTestId() = " + testId
        );

        return testId;
    }

    public void setTestId(int testId) {
        System.out.println(
                "BasePropertiesState.setTestId(" + testId + ")"
        );
        this.testId = testId;
    }

    public int getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(int batchCode) {
        this.batchCode = batchCode;
    }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductID() { return productID; }
    public void setProductID(String productID) { this.productID = productID; }

    public LocalDate getTestDate() { return testDate; }
    public void setTestDate(LocalDate testDate) { this.testDate = testDate; }

    public String getPlaceOfTesting() { return placeOfTesting; }
    public void setPlaceOfTesting(String placeOfTesting) { this.placeOfTesting = placeOfTesting; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public int getProjectId() {return projectId;}

    public void setProjectId(int projectId) {this.projectId = projectId;}

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public String getSop() {
        return sop;
    }

    public void setSop(String sop) {
        this.sop = sop;
    }
}
