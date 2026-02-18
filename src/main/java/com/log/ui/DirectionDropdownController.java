package com.log.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class DirectionDropdownController {

    @FXML
    private ComboBox<String> unitsCombo;

    @FXML
    public void initialize() {

        unitsCombo.setItems(FXCollections.observableArrayList(
                "Longitudinal", "Thickness direction", "Parallel",
                "Perpendicular", "XY",
                "Z", "U",
                "V", "W",
                "Radial", "Circumferential"
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

