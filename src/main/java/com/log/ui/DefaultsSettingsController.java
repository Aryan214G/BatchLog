package com.log.ui;

import com.log.database.DBUtil;
import com.log.model.DefaultProperty;
import com.log.service.CategoryService;
import com.log.service.DefaultPropertyService;
import com.log.service.PropertyUnitsService;
import com.log.service.UnitsService;
import com.log.ui.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import com.log.ui.CategoriesPageController;

import java.sql.Connection;
import java.util.List;

public class DefaultsSettingsController {

    @FXML
    private ComboBox<DefaultProperty> propertyComboBox;

    @FXML
    private ComboBox<String> unitComboBox;

    @FXML
    private TextField fields;

    private final DefaultPropertyService defaultPropertyService =
            new DefaultPropertyService();

    private final PropertyUnitsService propertyUnitsService =
            new PropertyUnitsService();

    private final UnitsService unitsService =
            new UnitsService();

    @FXML
    public void initialize() {

        loadProperties();

        propertyComboBox.setOnAction(event -> {

            DefaultProperty selected =
                    propertyComboBox.getValue();

            if(selected != null) {

                // Load rows
                fields.setText(
                        String.valueOf(selected.getRows())
                );

                // Load corresponding units
                loadUnitsForProperty(selected);
            }
        });
    }

    private void loadProperties() {

        List<DefaultProperty> properties =
                defaultPropertyService.getDefaults();

        propertyComboBox.getItems().addAll(properties);
    }

    private void loadUnitsForProperty(
            DefaultProperty property
    ) {

        unitComboBox.getItems().clear();

        try (
                Connection conn =
                        DBUtil.getConnection()
        ) {

            List<String> units =
                    propertyUnitsService
                            .getUnitsByProperty(
                                    conn,
                                    property.getPropertyId()
                            );

            unitComboBox
                    .getItems()
                    .addAll(units);

            // Select current default unit
            unitComboBox.setValue(
                    property.getUnit()
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void saveChanges() {

        DefaultProperty selected =
                propertyComboBox.getValue();

        if (selected == null) {

            AlertUtil.showWarning(
                    "Please select a property."
            );

            return;
        }

        String rowText =
                fields.getText().trim();

        if (rowText.isBlank()) {

            AlertUtil.showWarning(
                    "Please enter the number of rows."
            );

            return;
        }

        int rows;

        try {

            rows = Integer.parseInt(rowText);

        } catch (NumberFormatException e) {

            AlertUtil.showWarning(
                    "Please enter a valid numeric row count."
            );

            return;
        }

        if (rows < 1) {

            AlertUtil.showWarning(
                    "Rows must be at least 1."
            );

            return;
        }

        String selectedUnit =
                unitComboBox.getValue();

        if (selectedUnit == null
                || selectedUnit.isBlank()) {

            AlertUtil.showWarning(
                    "Please select a unit."
            );

            return;
        }

        int unitId =
                unitsService.getUnitIdByName(
                        selectedUnit
                );

        if (unitId == -1) {

            AlertUtil.showWarning(
                    "Invalid unit selected."
            );

            return;
        }

        try {

            // Update object

            selected.setRows(rows);
            selected.setUnit(selectedUnit);
            selected.setUnitId(unitId);

            // Update DB

            defaultPropertyService.updateDefaultProperty(
                    selected.getPropertyId(),
                    unitId,
                    rows
            );

            // Refresh state

            new CategoryService()
                    .refreshCategoriesState();

            propertyComboBox.getItems().clear();

            loadProperties();

            propertyComboBox.setValue(
                    propertyComboBox.getItems()
                            .stream()
                            .filter(
                                    p -> p.getPropertyId()
                                            == selected.getPropertyId()
                            )
                            .findFirst()
                            .orElse(null)
            );

            AlertUtil.showInfo(
                    "Default property updated successfully."
            );

            new CategoriesPageController()
                    .refreshcatmap();

        } catch (Exception e) {

            e.printStackTrace();

            AlertUtil.showError(
                    "Failed to update default property."
            );
        }

        new CategoriesPageController().refreshcatmap();
    }

    @FXML
    private void cancelChanges() {

        propertyComboBox
                .getSelectionModel()
                .clearSelection();

        unitComboBox
                .getSelectionModel()
                .clearSelection();

        fields.clear();
    }
}