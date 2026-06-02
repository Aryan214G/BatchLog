package com.log.model;

import java.util.ArrayList;
import java.util.List;

public class Batch {

    private int batchCode;
    private String batchId;
    private int productCode;
    private String sop;


    public Batch(int batchCode, String batchId, int productCode) {
        this.batchCode = batchCode;
        this.batchId = batchId;
        this.productCode = productCode;
    }

    public Batch(String batchId, int productCode) {
        this.batchId = batchId;
        this.productCode = productCode;
    }

    public int getBatchCode() { return batchCode; }
    public String getBatchId() { return batchId; }
    public int getProductCode() { return productCode; }
    public String getSop() {
        return sop;
    }

    public void setSop(String sop) {
        this.sop = sop;
    }
    // In Batch.java
    private List<BatchTest> tests = new ArrayList<>();

    public List<BatchTest> getTests() {
        return tests;
    }

}
