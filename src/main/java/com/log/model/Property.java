package com.log.model;

import java.util.List;

public class Property {

    // =========================
    // Core Property Fields
    // =========================

    private int propertyID;
    private String propertyName;

    // =========================
    // Related Entity IDs
    // =========================

    private int categoryID;
    private int unitID;
    private int testID;

    // =========================
    // Display Values
    // =========================

    private String categoryName;
    private String propertyUnit;

    // =========================
    // Related Objects
    // =========================

    private Temperature temperature;
    private Direction direction;

    // Optional: all values belonging to this property
    private List<PropertyValue> propertyValues;

    // =========================
    // Constructors
    // =========================

    public Property() {
    }

    public Property(int propertyID,
                    String propertyName,
                    int categoryID,
                    int unitID,
                    int testID) {

        this.propertyID = propertyID;
        this.propertyName = propertyName;
        this.categoryID = categoryID;
        this.unitID = unitID;
        this.testID = testID;
    }

    public Property(String propertyName,
                    int categoryID,
                    int unitID,
                    int testID) {

        this.propertyName = propertyName;
        this.categoryID = categoryID;
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

    public String getPropertyUnit() {
        return propertyUnit;
    }

    public void setPropertyUnit(String propertyUnit) {
        this.propertyUnit = propertyUnit;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public List<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(List<PropertyValue> propertyValues) {
        this.propertyValues = propertyValues;
    }
}