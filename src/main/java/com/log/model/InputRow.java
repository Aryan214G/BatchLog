package com.log.model;

import com.log.ui.UnitsDropdownController;
import javafx.scene.control.TextField;

public class InputRow {

    private TextField field;
    private UnitsDropdownController unit;


    public InputRow(TextField field, UnitsDropdownController unit) {
        this.field = field;
        this.unit = unit;
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
