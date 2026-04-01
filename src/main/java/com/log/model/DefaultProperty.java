package com.log.model;

public class DefaultProperty {

    private String propertyName;
    private int unitId;
    private int categoryId;
    private int rows;

    public DefaultProperty(String propertyName, int unitId, int categoryId, int rows) {
        this.propertyName = propertyName;
        this.unitId = unitId;
        this.categoryId = categoryId;
        this.rows = rows;
    }

    public String getPropertyName() {
        return propertyName;
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