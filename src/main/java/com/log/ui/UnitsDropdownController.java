package com.log.ui;

import com.log.database.DBUtil;
import com.log.service.DefaultPropertyService;
import com.log.service.PropertyUnitsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class UnitsDropdownController {

    private PropertyUnitsService propertyUnitsService = new PropertyUnitsService();
    private DefaultPropertyService defaultPropertyService = new DefaultPropertyService();

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

    public void setUnits(String property) {

        try (Connection conn = DBUtil.getConnection()) {

            //TODO: maybe store all the property unit mapping in a map instead of querying evertime
            int defPropId = defaultPropertyService.getPropertyId(conn, property);
        List<String> units = propertyUnitsService.getUnitsByProperty(conn, defPropId);

        unitsCombo.setItems(
                FXCollections.observableArrayList(units)
        );

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    public void setSelectedUnit(String unit) {
        unitsCombo.setValue(unit);
    }

    public ComboBox<String> getComboBox() {
        return unitsCombo;
    }
}

