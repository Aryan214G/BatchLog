
//used for creating objects for actual values retrieved from the DB

package com.log.model;

public class PropertyView {

    private int propertyId;
    private String propertyName;
    private String categoryName;
    private double temperature;
    private String direction;
    private String unit;

    public PropertyView(int propertyId, String propertyName,
                        String categoryName, double temperature,
                        String direction, String unit) {
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.categoryName = categoryName;
        this.temperature = temperature;
        this.direction = direction;
        this.unit = unit;
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

    public String getDirection() {
        return direction;
    }

    public String getUnit() {
        return unit;
    }
}
