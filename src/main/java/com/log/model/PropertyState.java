package com.log.model;

import java.util.ArrayList;
import java.util.List;

public class PropertyState {

    private List<Reading> readings = new ArrayList<>();

    private String temperature;
    private String temperatureUnit;
    private String direction;

    public List<Reading> getReadings() {
        return readings;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public String getDirection() {
        return direction;
    }
}