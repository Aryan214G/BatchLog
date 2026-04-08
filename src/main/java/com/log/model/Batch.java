package com.log.model;

public class Batch {

    private int batchCode;
    private int batchId;
    private int projectId;
    private int productCode;


    public Batch(int batchCode, int batchId, int projectId, int productCode) {
        this.batchCode = batchCode;
        this.batchId = batchId;
        this.projectId = projectId;
        this.productCode = productCode;
    }

    public Batch(int batchId, int projectId, int productCode) {
        this.batchId = batchId;
        this.projectId = projectId;
        this.productCode = productCode;
    }

    public int getBatchCode() { return batchCode; }
    public int getBatchId() { return batchId; }
    public int getProjectId() { return projectId; }
    public int getProductCode() { return productCode; }

}
