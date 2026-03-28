package com.log.model;

import java.util.ArrayList;
import java.util.List;

public class PropertyState {

    private List<Reading> readings = new ArrayList<>();

    private Temperature temperature;
    private String direction;

    public List<Reading> getReadings() {
        return readings;
    }

    public void setReadings(List<Reading> readings) {
        this.readings = readings;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Temperature getTemperature() {
        return temperature;
    }


    public String getDirection() {
        return direction;
    }
}