package com.log.model;

import java.util.List;

public class Property {

    // =========================
    // Core Property Fields
    // =========================

    private int propertyID;
    private String propertyName;

    private int testID;

    // =========================
    // Related Objects
    // =========================

    private Unit unit;
    private Category category;
    private Temperature temperature;
    private Direction direction;

    // =========================
    // Property Values
    // =========================

    private List<PropertyValue> propertyValues;

    // =========================
    // Constructors
    // =========================

    public Property() {
    }

    public Property(int propertyID,
                    String propertyName,
                    Unit unit,
                    int testID,
                    Category category,
                    Temperature temperature,
                    Direction direction) {

        this.propertyID = propertyID;
        this.propertyName = propertyName;
        this.unit = unit;
        this.testID = testID;
        this.category = category;
        this.temperature = temperature;
        this.direction = direction;
    }

    public Property(String propertyName,
                    Unit unit,
                    int testID,
                    Category category,
                    Temperature temperature,
                    Direction direction) {

        this.propertyName = propertyName;
        this.unit = unit;
        this.testID = testID;
        this.category = category;
        this.temperature = temperature;
        this.direction = direction;
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

    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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