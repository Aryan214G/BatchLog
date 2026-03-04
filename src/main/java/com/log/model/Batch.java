package com.log.model;

public class Batch {

    private int batchCode;
    private int batchId;
    private String testDate;
    private String testSite;
    private int projectId;
    private int productCode;

    public Batch(int batchCode, int batchId, String testDate,
                 String testSite, int projectId, int productCode) {

        this.batchCode = batchCode;
        this.batchId = batchId;
        this.testDate = testDate;
        this.testSite = testSite;
        this.projectId = projectId;
        this.productCode = productCode;
    }

    public int getBatchCode() { return batchCode; }
    public int getBatchId() { return batchId; }
    public String getTestDate() { return testDate; }
    public String getTestSite() { return testSite; }
    public int getProjectId() { return projectId; }
    public int getProductCode() { return productCode; }

}
