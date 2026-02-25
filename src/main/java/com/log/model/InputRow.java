package com.log.model;

import com.log.ui.DirectionDropdownController;
import com.log.ui.UnitsDropdownController;
import javafx.scene.control.TextField;

public class InputRow {

    private TextField field;
    private UnitsDropdownController unit;
    private String fieldValue;
    private String unitValue;
    private TextField temperatureField;
    private DirectionDropdownController direction;
    private UnitsDropdownController tempUnit;
    private String temperatureValue;
    private String directionValue;
    private String tempUnitValue;

    public InputRow(String fieldValue, String unitValue, String temperatureValue, String tempUnitValue, String directionValue) {
        this.fieldValue = fieldValue;
        this.unitValue = unitValue;
        this.temperatureValue = temperatureValue;
        this.tempUnitValue = tempUnitValue;
        this.directionValue = directionValue;
    }

    public TextField getTemperatureField() {
        return temperatureField;
    }

    public void setTemperatureField(TextField temperatureField) {
        this.temperatureField = temperatureField;
    }

    public DirectionDropdownController getDirection() {
        return direction;
    }

    public void setDirection(DirectionDropdownController direction) {
        this.direction = direction;
    }

    public String getTemperatureValue() {
        return temperatureValue;
    }

    public void setTemperatureValue(String temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public String getDirectionValue() {
        return directionValue;
    }

    public void setDirectionValue(String directionValue) {
        this.directionValue = directionValue;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(String unitValue) {
        this.unitValue = unitValue;
    }

    public InputRow(TextField field, UnitsDropdownController unit) {
        this.field = field;
        this.unit = unit;
    }

    public InputRow(String fieldValue, String unitValue) {
        this.fieldValue = fieldValue;
        this.unitValue = unitValue;
    }

    public InputRow(TextField temperatureField, UnitsDropdownController tempUnit, DirectionDropdownController direction) {
        this.temperatureField = temperatureField;
        this.tempUnit = tempUnit;
        this.direction = direction;
    }

    public TextField getField() {
        return field;
    }

    public void setField(TextField field) {
        this.field = field;
    }

    public UnitsDropdownController getUnit() {
        return unit;
    }

    public void setUnit(UnitsDropdownController unit) {
        this.unit = unit;
    }

    public UnitsDropdownController getTempUnit() {
        return tempUnit;
    }

    public void setTempUnit(UnitsDropdownController tempUnit) {
        this.tempUnit = tempUnit;
    }

    public String getTempUnitValue() {
        return tempUnitValue;
    }

    public void setTempUnitValue(String tempUnitValue) {
        this.tempUnitValue = tempUnitValue;
    }
}
