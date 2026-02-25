package com.log.core;

import com.log.model.InputRow;

import java.util.ArrayList;
import java.util.List;

public class PropertyState {

    private List<InputRow> readings = new ArrayList<>();
    private String temperature;
    private String temperatureUnit;
    private String direction;

    public List<InputRow> getReadings() {
        return readings;
    }

    public void setReadings(List<InputRow> readings) {
        this.readings = readings;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}