package com.log.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class UnitsDropdownController {

    @FXML
    private ComboBox<String> unitsCombo;

    @FXML
    public void initialize() {

        unitsCombo.setItems(FXCollections.observableArrayList(
                "mm", "cm", "m",
                "kg", "g",
                "N", "kN",
                "MPa", "GPa",
                "°C", "K",
                "s", "min"
        ));
    }

    public String getSelectedUnit() {
        return unitsCombo.getValue();
    }

    public void setSelectedUnit(String unit) {
        unitsCombo.setValue(unit);
    }

    public ComboBox<String> getComboBox() {
        return unitsCombo;
    }
}

