package com.log.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.HashMap;

public class UnitsDropdownController {

    @FXML
    private ComboBox<String> unitsCombo;

    private HashMap<String, ObservableList<String>> unitsMap = new HashMap<>();
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

    private void populateUnitsMap() {

        // ===================== Physical =====================

        unitsMap.put("Density",
                FXCollections.observableArrayList(
                        "g/cc",
                        "kg/m^3"
                ));

        unitsMap.put("Open porosity",
                FXCollections.observableArrayList(
                        "%"
                ));

        // ===================== Mechanical =====================
        // (Same units for ALL mechanical subproperties)

        ObservableList<String> mechanicalUnits =
                FXCollections.observableArrayList(
                        "Mpa",
                        "Gpa",
                        "KG/cm^2",
                        "%",
                        "N/mm^2"
                );

        unitsMap.put("Tensile Strength", mechanicalUnits);
        unitsMap.put("Tensile Modulus", mechanicalUnits);
        unitsMap.put("Compressive Strength", mechanicalUnits);
        unitsMap.put("Compressive Modulus", mechanicalUnits);
        unitsMap.put("Flexural Strength", mechanicalUnits);
        unitsMap.put("Flexural Modulus", mechanicalUnits);

        // ===================== Thermal =====================

        unitsMap.put("Specific Heat",
                FXCollections.observableArrayList(
                        "cal/g^o c",
                        "J/g.k",
                        "J/Kg.k",
                        "KJ/Kg.k"
                ));

        unitsMap.put("Thermal Diffusivity",
                FXCollections.observableArrayList(
                        "mm^2/sec",
                        "cm^2/sec"
                ));

        unitsMap.put("Thermal conductivity",
                FXCollections.observableArrayList(
                        "W/m.k",
                        "W/mm.k"
                ));

        unitsMap.put("Mass Loss(%)",
                FXCollections.observableArrayList(
                        "%"
                ));

        unitsMap.put("Coefficient of thermal expansion",
                FXCollections.observableArrayList(
                        "CTE",
                        "10^-6/^oc",
                        "um/m.^oc"
                ));

        // ===================== Tribological =====================

        unitsMap.put("Coefficient of friction",
                FXCollections.observableArrayList(
                        "u"
                ));

        unitsMap.put("Wear Rate",
                FXCollections.observableArrayList(
                        "um/F/S",
                        "g/m",
                        "g/F/S"
                ));

        // ===================== Microstructure =====================

        unitsMap.put("Grain size",
                FXCollections.observableArrayList(
                        "um"
                ));

        unitsMap.put("ASTM grain size no.",
                FXCollections.observableArrayList(
                        "ASTM"
                ));
    }

    public void setUnits(String property) {
        populateUnitsMap();
        unitsCombo.setItems(unitsMap.get(property));
    }

    public void setSelectedUnit(String unit) {
        unitsCombo.setValue(unit);
    }

    public ComboBox<String> getComboBox() {
        return unitsCombo;
    }
}

