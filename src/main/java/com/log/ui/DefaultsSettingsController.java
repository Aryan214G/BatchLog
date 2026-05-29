package com.log.ui;

import com.log.database.DBUtil;
import com.log.model.DefaultProperty;
import com.log.service.CategoryService;
import com.log.service.DefaultPropertyService;
import com.log.service.PropertyUnitsService;
import com.log.service.UnitsService;
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

        if(selected == null) {
            return;
        }

        int rows;

        try {

            rows = Integer.parseInt(
                    fields.getText().trim()
            );

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid row count."
            );

            return;
        }

        if(rows < 1) {

            System.out.println(
                    "Rows must be at least 1."
            );

            return;
        }

        String selectedUnit =
                unitComboBox.getValue();

        if(selectedUnit == null
                || selectedUnit.isBlank()) {

            System.out.println(
                    "Please select a unit."
            );

            return;
        }

        // Convert unit name -> unit id
        int unitId =
                unitsService.getUnitIdByName(
                        selectedUnit
                );

        if(unitId == -1) {

            System.out.println(
                    "Invalid unit selected."
            );

            return;
        }

        // Update in-memory object
        selected.setRows(rows);
        selected.setUnit(selectedUnit);
        selected.setUnitId(unitId);

        // Update DB
        defaultPropertyService.updateDefaultProperty(
                selected.getPropertyId(),
                unitId,
                rows
        );
        //debugging
        System.out.println(defaultPropertyService.getDefaults());
        new CategoryService()
                .refreshCategoriesState();


        propertyComboBox.getItems().clear();
        loadProperties();

        propertyComboBox.setValue(
                propertyComboBox.getItems()
                        .stream()
                        .filter(p -> p.getPropertyId()
                                == selected.getPropertyId())
                        .findFirst()
                        .orElse(null)
        );

        System.out.println(
                "Updated successfully."
        );

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