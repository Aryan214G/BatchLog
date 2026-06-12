package com.log.ui;

import com.log.model.PropertyState;
import com.log.model.PropertyValue;
import com.log.model.Unit;
import javafx.scene.control.TextField;

public class InputRow {

    private TextField field;
    private TextField componentNumberField;
    private UnitsDropdownController unitController;
    private PropertyValue propertyValue;

    public InputRow() {
        this.propertyValue = new PropertyValue();
    }

    public InputRow(TextField field, UnitsDropdownController unitController) {
        this.field = field;
        this.unitController = unitController;
        this.propertyValue = new PropertyValue();
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

    public void setField(TextField field) {
        this.field = field;
    }

    public void setUnitController(UnitsDropdownController unitController) {
        this.unitController = unitController;
    }

    public TextField getComponentNumberField() {
        return componentNumberField;
    }

    public void setComponentNumberField(TextField componentNumberField) {
        this.componentNumberField = componentNumberField;
    }
}