package com.log.ui.settings;

import com.log.database.DBUtil;
import com.log.model.Direction;
import com.log.model.Temperature;
import com.log.model.Unit;
import com.log.service.DirectionService;
import com.log.service.TemperatureUnitService;
import com.log.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.Connection;

public class Addtempunitscontroller {

    private final TemperatureUnitService temperatureUnitService =
            new TemperatureUnitService();

    private final DirectionService directionService =
            new DirectionService();

    @FXML
    private TextField temperatureUnitField;

    @FXML
    private TextField directionField;

    @FXML
    private ListView<String> temperatureUnitList;

    @FXML
    private ListView<String> directionList;

    @FXML
    public void initialize() {

        loadTemperatureUnits();
        loadDirections();

        temperatureUnitList.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        temperatureUnitField.setText(
                                newVal
                        );
                    }
                });

        directionList.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        directionField.setText(
                                newVal
                        );
                    }
                });
    }

    @FXML
    private void handleAddTemperatureUnit() {

        String unit = temperatureUnitField.getText();

        if (unit == null || unit.trim().isBlank()) {

            AlertUtil.showWarning(
                    "Please enter a temperature unit."
            );
            return;
        }

        String trimmedUnit = unit.trim();

        boolean exists =
                temperatureUnitList.getItems()
                        .stream()
                        .anyMatch(
                                u -> u.equalsIgnoreCase(trimmedUnit)
                        );

        if (exists) {
            AlertUtil.showWarning(
                    "Temperature unit already exists."
            );
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {

            Unit tempUnit = new Unit();
            tempUnit.setUnit(trimmedUnit);

            temperatureUnitService.insertTemperatureUnit(
                    conn,
                    tempUnit
            );

            loadTemperatureUnits();

            temperatureUnitField.clear();

            AlertUtil.showInfo(
                    "Temperature unit added successfully."
            );

        } catch (Exception e) {

            e.printStackTrace();

            AlertUtil.showError(
                    "Failed to add temperature unit."
            );
        }
    }

    @FXML
    private void handleAddDirection() {

        String direction = directionField.getText();

        if (direction == null || direction.trim().isBlank()) {

            AlertUtil.showWarning(
                    "Please enter a direction."
            );

            return;
        }

       String trimmeddirection = direction.trim();

        boolean exists =
                directionList.getItems()
                        .stream()
                        .anyMatch(
                                d -> d.equalsIgnoreCase(trimmeddirection)
                        );
        if (exists) {
            AlertUtil.showWarning(
                    "Direction already exists."
            );
            return;
        }

        try {

            Direction directionobj = new Direction();
            directionobj.setDirVal(trimmeddirection);

            directionService.createDirection(directionobj);

            loadDirections();

            directionField.clear();

            AlertUtil.showInfo(
                    "Direction added successfully."
            );

        } catch (Exception e) {

            e.printStackTrace();

            AlertUtil.showError(
                    "Failed to add direction."
            );
        }
    }

    private void loadTemperatureUnits() {

        temperatureUnitList.getItems().clear();

        try (Connection conn = DBUtil.getConnection()) {

            temperatureUnitList.getItems().addAll(
                    temperatureUnitService
                            .getAllTemperatureUnits(conn)
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void loadDirections() {

        directionList.getItems().clear();

        directionList.getItems().addAll(
                directionService.getAllDirections().stream().map(Direction::getDirVal)
                        .toList()
        );
    }

    @FXML
    private void handleClose() {

        temperatureUnitField.getScene()
                .getWindow()
                .hide();
    }

    @FXML
    private void handleRenameTemperatureUnit() {

        String selected =
                temperatureUnitList
                        .getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {

            AlertUtil.showWarning(
                    "Please select a temperature unit."
            );

            return;
        }

        String newName =
                temperatureUnitField.getText();

        if (newName == null ||
                newName.trim().isBlank()) {

            AlertUtil.showWarning(
                    "Please enter a new name."
            );

            return;
        }

        try (Connection conn = DBUtil.getConnection()) {

            Unit unit =
                    temperatureUnitService
                            .getTemperatureUnit(
                                    conn,
                                    selected
                            );

            temperatureUnitService
                    .updateTemperatureUnit(
                            unit.getUnitId(),
                            newName.trim()
                    );

            loadTemperatureUnits();
            temperatureUnitField.clear();
            AlertUtil.showInfo(
                    "Temperature unit renamed successfully."
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void handleRenameDirection() {

        String selectedDirection =
                directionList.getSelectionModel()
                        .getSelectedItem();

        if (selectedDirection == null) {

            AlertUtil.showWarning(
                    "Please select a direction."
            );

            return;
        }

        String newDirection =
                directionField.getText();

        if (newDirection == null ||
                newDirection.trim().isBlank()) {

            AlertUtil.showWarning(
                    "Please enter a new direction."
            );

            return;
        }

        try (Connection conn =
                     DBUtil.getConnection()) {

            Direction direction =
                    directionService.getDirectionByName(
                            conn,
                            selectedDirection
                    );

            direction.setDirVal(
                    newDirection.trim()
            );

            directionService.updateDirection(
                    direction
            );

            loadDirections();

            directionField.clear();

            AlertUtil.showInfo(
                    "Direction renamed successfully."
            );

        } catch (Exception e) {

            e.printStackTrace();

            AlertUtil.showError(
                    "Failed to rename direction."
            );
        }
    }
}