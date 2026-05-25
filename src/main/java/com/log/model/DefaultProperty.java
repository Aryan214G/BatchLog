package com.log.model;

public class DefaultProperty {

    private int propertyId;

    private String propertyName;

    private String categoryName;

    private int unitId;

    private int categoryId;

    private int rows;

    // =========================================
    // Constructors
    // =========================================

    public DefaultProperty(String propertyName,
                           int unitId,
                           int categoryId,
                           int rows) {

        this.propertyName = propertyName;
        this.unitId = unitId;
        this.categoryId = categoryId;
        this.rows = rows;
    }

    public DefaultProperty(int propertyId,
                           String propertyName,
                           String categoryName,
                           int rows) {

        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.categoryName = categoryName;
        this.rows = rows;
    }

    @Override
    public String toString() {
        return propertyName;
    }

    // =========================================
    // Getters
    // =========================================

    public int getPropertyId() {
        return propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getUnitId() {
        return unitId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getRows() {
        return rows;
    }
}