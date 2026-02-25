package com.log.model;

import com.log.ui.UnitsDropdownController;
import javafx.scene.control.TextField;

public class InputRow {

    private TextField field;
    private UnitsDropdownController unit;
    private String fieldValue;
    private String unitValue;

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
}
