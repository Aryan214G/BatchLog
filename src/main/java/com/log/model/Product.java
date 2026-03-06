package com.log.model;

public class Product {

    private int productCode;
    private int productId;
    private String productName;
    private int projectId;

    public Product() {
    }

    public Product(int productId, String productName, int projectId) {
        this.productId = productId;
        this.productName = productName;
        this.projectId = projectId;
    }

    public Product(int productCode, int productId, String productName, int projectId) {
        this.productCode = productCode;
        this.productId = productId;
        this.productName = productName;
        this.projectId = projectId;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}