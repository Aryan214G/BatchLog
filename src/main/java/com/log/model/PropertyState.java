package com.log.model;

import com.log.ui.InputRow;

import java.util.ArrayList;
import java.util.List;

public class PropertyState {

    private List<Reading> readings = new ArrayList<>();
    private List<InputRow> inputRows = new ArrayList<>();
    private Temperature temperature;
    private Direction direction;
    private Unit unit;
    private String testMethod;

    public PropertyState() {
    }

    public PropertyState(List<InputRow> inputRows, Temperature temperature, Direction direction) {
        this.inputRows = inputRows;
        this.temperature = temperature;
        this.direction = direction;
    }

    public List<Reading> getReadings() {
        return readings;
    }

    public void setReadings(List<Reading> readings) {
        this.readings = readings;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public List<InputRow> getInputRows() {
        return inputRows;
    }

    public void setInputRows(List<InputRow> inputRows) {
        this.inputRows = inputRows;
    }

    public Direction getDirection() {
        return direction;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getTestMethod() {
        return testMethod;
    }

    public void setTestMethod(String testMethod) {
        this.testMethod = testMethod;
    }
}