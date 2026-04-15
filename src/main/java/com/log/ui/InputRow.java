package com.log.ui;

import com.log.model.PropertyState;
import com.log.model.PropertyValue;
import javafx.scene.control.TextField;

public class InputRow {

    private TextField field;
    private UnitsDropdownController unitController;
    private PropertyValue propertyValue;

    public InputRow() {
    }

    public InputRow(TextField field, UnitsDropdownController unitController) {
        this.field = field;
        this.unitController = unitController;
    }

    public TextField getField() {
        return field;
    }

    public UnitsDropdownController getUnitController() {
        return unitController;
    }

    public PropertyValue getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(PropertyValue propertyValue) {
        this.propertyValue = propertyValue;
    }
}