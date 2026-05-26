package com.log.model;

public class Reading {

    //TODO: Might need to convert value datatype to double after finishing this task
    // in frontend section in reminders: "Only take double values in fields and set alerts"
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