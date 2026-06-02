package com.log.model;

import java.util.ArrayList;
import java.util.List;

public class PropertyRow {

    private int propertyId;
    private String name;
    private String category;
    private String temperature;
    private String direction;
    private String unit;

    private List<Double> values;


    public PropertyRow() {
        this.values = new ArrayList<>();
    }

    public PropertyRow(String name,
                       String category,
                       String temperature,
                       String direction,
                       String unit) {
        this.name = name;
        this.category = category;
        this.temperature = temperature;
        this.direction = direction;
        this.unit = unit;
        this.values = new ArrayList<>();
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<Double> getValues() {
        return values;
    }

     public void setValues(List<Double> values) {
        this.values = values;
    }


    public double getAverage() {
        return values.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public String toString() {
        return "PropertyRow{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", temperature='" + temperature + '\'' +
                ", direction='" + direction + '\'' +
                ", unit='" + unit + '\'' +
                ", values=" + values +
                '}';
    }
}
