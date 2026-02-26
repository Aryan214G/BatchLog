package com.log.ui;

import javafx.scene.control.TextField;

public class InputRow {

    private final TextField field;
    private final UnitsDropdownController unitController;

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
}