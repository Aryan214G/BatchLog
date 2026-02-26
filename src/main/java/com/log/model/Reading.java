package com.log.model;

public class Reading {

    private String value;
    private String unit;

    public Reading(String value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }
}