package com.log.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class DirectionDropdownController {

    @FXML
    private ComboBox<String> directionCombo;

    @FXML
    public void initialize() {

        directionCombo.setItems(FXCollections.observableArrayList(
                "Longitudinal", "Thickness direction", "Parallel",
                "Perpendicular", "XY",
                "Z", "U",
                "V", "W",
                "Radial", "Circumferential"
        ));
    }


    public String getSelectedUnit() {
        return directionCombo.getValue();
    }

    public void setSelectedUnit(String unit) {
        directionCombo.setValue(unit);
    }

    public ComboBox<String> getComboBox() {
        return directionCombo;
    }
}

