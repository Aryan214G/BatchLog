package com.log.ui.settings;

import com.log.database.DBUtil;
import com.log.model.DefaultProperty;
import com.log.model.Unit;
import com.log.service.DefaultPropertyService;
import com.log.service.PropertyUnitsService;
import com.log.service.UnitsService;
import com.log.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.util.List;

    public class UnitsSettingsController {

        @FXML
        private ComboBox<DefaultProperty> propertyComboBox;
        @FXML
        private ListView<String> unitsListView;
        @FXML
        private TextField newUnitField;
        @FXML
        private ComboBox<String> availableUnitsComboBox;

        private final DefaultPropertyService
                defaultPropertyService =
                new DefaultPropertyService();

        private final PropertyUnitsService
                propertyUnitsService =
                new PropertyUnitsService();

        private final UnitsService
                unitsService =
                new UnitsService();

        @FXML
        public void initialize() {

            loadProperties();

            loadAllUnits();

            propertyComboBox.setOnAction(
                    e -> loadUnitsForSelectedProperty()
            );
        }

        private void loadProperties() {

            propertyComboBox
                    .getItems()
                    .addAll(
                            defaultPropertyService
                                    .getDefaults()
                    );
        }

        private void loadAllUnits() {

            List<Unit> units =
                    unitsService.getAllUnits();

            for(Unit unit : units) {

                availableUnitsComboBox
                        .getItems()
                        .add(unit.getUnit());
            }
        }

        private void loadUnitsForSelectedProperty() {

            DefaultProperty property =
                    propertyComboBox.getValue();

            if(property == null) {
                return;
            }

            try(Connection conn =
                        DBUtil.getConnection()) {

                List<String> units =
                        propertyUnitsService
                                .getUnitsByProperty(
                                        conn,
                                        property.getPropertyId()
                                );

                unitsListView
                        .getItems()
                        .setAll(units);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        @FXML
        private void addUnit() {

            DefaultProperty property =
                    propertyComboBox.getValue();

            if (property == null) {

                AlertUtil.showWarning(
                        "Please select a property."
                );

                return;
            }

            String customUnit =
                    newUnitField.getText().trim();

            String unitName =
                    availableUnitsComboBox.getValue();

            // BOTH entered

            if (!customUnit.isBlank()
                    && unitName != null
                    && !unitName.isBlank()) {

                AlertUtil.showWarning(
                        "Please either select an existing unit OR enter a custom unit."
                );

                return;
            }

            // NEITHER entered

            if (customUnit.isBlank()
                    && (unitName == null
                    || unitName.isBlank())) {

                AlertUtil.showWarning(
                        "Please select a unit or enter a custom unit."
                );

                return;
            }

            try (Connection conn =
                         DBUtil.getConnection()) {

                // =====================================
                // CUSTOM UNIT PATH
                // =====================================

                if (!customUnit.isBlank()) {

                    int unitId;

                    if (unitsService.unitExists(
                            conn,
                            customUnit
                    )) {

                        unitId =
                                unitsService.getUnitIdByName(
                                        customUnit
                                );

                    } else {

                        unitId =
                                unitsService.insertUnit(
                                        conn,
                                        customUnit
                                );
                    }

                    if (propertyUnitsService
                            .unitExistsForProperty(
                                    conn,
                                    property.getPropertyId(),
                                    unitId
                            )) {

                        AlertUtil.showWarning(
                                "This unit is already assigned to the selected property."
                        );

                        return;
                    }

                    propertyUnitsService
                            .addUnitToProperty(
                                    conn,
                                    property.getPropertyId(),
                                    unitId
                            );

                    AlertUtil.showInfo(
                            "Custom unit added successfully."
                    );

                    newUnitField.clear();

                    availableUnitsComboBox
                            .getItems()
                            .clear();

                    loadAllUnits();

                    loadUnitsForSelectedProperty();

                    return;
                }

                // =====================================
                // EXISTING UNIT PATH
                // =====================================

                int unitId =
                        unitsService.getUnitIdByName(
                                unitName
                        );

                if (propertyUnitsService
                        .unitExistsForProperty(
                                conn,
                                property.getPropertyId(),
                                unitId
                        )) {

                    AlertUtil.showWarning(
                            "This unit is already assigned to the selected property."
                    );

                    return;
                }

                propertyUnitsService
                        .addUnitToProperty(
                                conn,
                                property.getPropertyId(),
                                unitId
                        );

                AlertUtil.showInfo(
                        "Unit added successfully."
                );

                loadUnitsForSelectedProperty();

            } catch (Exception e) {

                e.printStackTrace();

                AlertUtil.showError(
                        "Failed to add unit."
                );
            }
        }

        @FXML
        private void removeUnit() {

            DefaultProperty property =
                    propertyComboBox.getValue();

            if (property == null) {

                AlertUtil.showWarning(
                        "Please select a property."
                );

                return;
            }

            String selectedUnit =
                    unitsListView
                            .getSelectionModel()
                            .getSelectedItem();

            if (selectedUnit == null) {

                AlertUtil.showWarning(
                        "Please select a unit to remove."
                );

                return;
            }

            int unitId =
                    unitsService.getUnitIdByName(
                            selectedUnit
                    );

            if (property.getUnitId() == unitId) {

                AlertUtil.showWarning(
                        "Cannot remove the current default unit. Change the default unit first."
                );

                return;
            }

            try (Connection conn =
                         DBUtil.getConnection()) {

                propertyUnitsService
                        .removeUnitFromProperty(
                                conn,
                                property.getPropertyId(),
                                unitId
                        );

                AlertUtil.showInfo(
                        "Unit removed successfully."
                );

                loadUnitsForSelectedProperty();

            } catch (Exception e) {

                e.printStackTrace();

                AlertUtil.showError(
                        "Failed to remove unit."
                );
            }
        }
    }
