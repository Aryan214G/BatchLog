
//used for creating objects for actual values retrieved from the DB

package com.log.model;

public class PropertyView {

    private int propertyId;
    private String propertyName;
    private String categoryName;
    private double temperature;
    private String tempUnit;
    private String direction;
    private String unit;
    private int rows;

    public PropertyView(int propertyId, String propertyName,
                        String categoryName, double temperature, String tempUnit,
                        String direction, String unit) {
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.categoryName = categoryName;
        this.temperature = temperature;
        this.tempUnit = tempUnit;
        this.direction = direction;
        this.unit = unit;
    }

    public PropertyView(int propertyId, String propertyName, String categoryName, int rows) {
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.categoryName = categoryName;
        this.rows = rows;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getTemperature() {
        return temperature;
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

    public String getUnit() {
        return unit;
    }

    public int getRows() { return rows; }

    @Override
    public String toString() {
        return propertyName;
    }
}
