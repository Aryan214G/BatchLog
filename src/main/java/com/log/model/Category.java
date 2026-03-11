package com.log.model;

public class Category {

    private int categoryId;
    private String categoryName;

    // Empty constructor
    public Category() {}

    // Constructor used when creating a new category
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    // Constructor used when reading from DB
    public Category(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}