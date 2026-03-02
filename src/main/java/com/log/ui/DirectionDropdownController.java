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
                "Not Applicable","Longitudinal", "Thickness direction", "Parallel", "Perpendicular", "XY", "Z", "U", "V", "W", "Radial", "Circumferential", "YZ", "ZX","12","23","31"
        ));
    }


    public String getSelectedDirection() {
        return directionCombo.getValue();
    }

    public void setSelectedDirection(String unit) {
        directionCombo.setValue(unit);
    }

    public ComboBox<String> getComboBox() {
        return directionCombo;
    }
}

