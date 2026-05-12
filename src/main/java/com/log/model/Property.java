package com.log.model;

public class Property {

    // =========================
    // Database fields
    // =========================

    private int propertyID;
    private String propertyName;

    private int categoryID;
    private int tempID;
    private int dirID;
    private int unitID;

    private int testID;

    // =========================
    // Display/UI fields
    // =========================

    private String categoryName;

    private double tempValue;
    private String tempUnit;

    private String direction;

    private String propertyUnit;

    public Property() {
    }

    public Property(String propertyName, int categoryID, int tempID, int dirID, int unitID, int testID) {
        this.propertyName = propertyName;
        this.categoryID = categoryID;
        this.tempID = tempID;
        this.dirID = dirID;
        this.unitID = unitID;
        this.testID = testID;
    }

    // =========================
    // Getters and Setters
    // =========================

    public int getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(int propertyID) {
        this.propertyID = propertyID;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getTempID() {
        return tempID;
    }

    public void setTempID(int tempID) {
        this.tempID = tempID;
    }

    public int getDirID() {
        return dirID;
    }

    public void setDirID(int dirID) {
        this.dirID = dirID;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getTempValue() {
        return tempValue;
    }

    public void setTempValue(double tempValue) {
        this.tempValue = tempValue;
    }

    public String getTempUnit() {
        return tempUnit;
    }

    public void setTempUnit(String tempUnit) {
        this.tempUnit = tempUnit;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPropertyUnit() {
        return propertyUnit;
    }

    public void setPropertyUnit(String propertyUnit) {
        this.propertyUnit = propertyUnit;
    }
}