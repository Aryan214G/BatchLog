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

                // Length
                "mm", "cm", "m", "µm",

                // Mass
                "kg", "g",

                // Force
                "N", "kN",

                // Density
                "g/cc", "kg/m³",

                // Mechanical
                "MPa", "GPa", "kg/cm²", "N/mm²",

                // Temperature
                "°C", "K",

                // Time
                "s", "min",

                // Percentage / Dimensionless
                "%", "µ",

                // Specific Heat
                "cal/g°C", "J/g·K", "J/kg·K", "kJ/kg·K",

                // Thermal Diffusivity
                "mm²/s", "cm²/s",

                // Thermal Conductivity
                "W/m·K", "W/mm·K",

                // Coefficient of Thermal Expansion
                "10⁻⁶/°C", "µm/m·°C",

                // Wear Rate
                "µm/F/S", "g/m", "g/F/S"
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

