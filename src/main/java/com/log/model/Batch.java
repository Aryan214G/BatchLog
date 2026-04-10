package com.log.model;

public class Batch {

    private int batchCode;
    private String batchId;
    private int projectId;
    private int productCode;


    public Batch(int batchCode, String batchId, int projectId, int productCode) {
        this.batchCode = batchCode;
        this.batchId = batchId;
        this.projectId = projectId;
        this.productCode = productCode;
    }

    public Batch(String batchId, int projectId, int productCode) {
        this.batchId = batchId;
        this.projectId = projectId;
        this.productCode = productCode;
    }

    public int getBatchCode() { return batchCode; }
    public String getBatchId() { return batchId; }
    public int getProjectId() { return projectId; }
    public int getProductCode() { return productCode; }

}
