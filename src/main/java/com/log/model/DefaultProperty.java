package com.log.model;

public class DefaultProperty {

    private int propertyId;

    private String propertyName;

    private String categoryName;

    private int unitId;

    private String unit;

    private int categoryId;

    private int rows;

    private int hasComponentNumber;


    // =========================================
    // Constructors
    // =========================================


    public DefaultProperty() {
    }

    public DefaultProperty(String propertyName,
                           int unitId,
                           String unit,
                           int rows,
                           int hasComponentNumber) {
        this.propertyName= propertyName;
        this.unitId =  unitId;
        this.unit= unit;
        this.rows = rows;
        this.hasComponentNumber = hasComponentNumber;
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

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getHasComponentNumber() {
        return hasComponentNumber;
    }

    public void setHasComponentNumber(int hasComponentNumber) {
        this.hasComponentNumber = hasComponentNumber;
    }
}