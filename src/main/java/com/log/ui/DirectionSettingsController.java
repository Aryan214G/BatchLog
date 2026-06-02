package com.log.ui;

import com.log.database.DBUtil;
import com.log.model.Direction;
import com.log.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import com.log.service.DirectionService;

import java.sql.Connection;
import java.util.List;

public class DirectionSettingsController {
    @FXML
    private ListView<String> directionsListView;

    @FXML
    private TextField directionField;

    DirectionService directionService= new DirectionService();

    private void loadDirections() {

        List<Direction> directions =
                directionService.getAllDirections();

        directionsListView.getItems().clear();

        for (Direction direction : directions) {

            directionsListView.getItems().add(
                    direction.getDirVal()
            );
        }
    }

    @FXML
    public void initialize() {

        loadDirections();
    }
    @FXML
    private void addDirection() {

        String value =
                directionField.getText().trim();

        if (value.isBlank()) {

            AlertUtil.showWarning(
                    "Please enter a direction."
            );

            return;
        }

        try (Connection conn =
                     DBUtil.getConnection()) {

            if (directionService.directionExists(
                    conn,
                    value
            )) {

                AlertUtil.showWarning(
                        "Direction already exists."
                );

                return;
            }

            directionService.createDirection(
                    new Direction(value)
            );

            AlertUtil.showInfo(
                    "Direction added successfully."
            );

            directionField.clear();

            loadDirections();

        } catch (Exception e) {

            e.printStackTrace();

            AlertUtil.showError(
                    "Failed to add direction."
            );
        }
    }
    @FXML
    private void removeDirection() {

        String selected =
                directionsListView
                        .getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {

            AlertUtil.showWarning(
                    "Please select a direction."
            );

            return;
        }

        try (Connection conn =
                     DBUtil.getConnection()) {

            directionService.deleteDirection(
                    conn,
                    selected
            );

            AlertUtil.showInfo(
                    "Direction removed successfully."
            );

            loadDirections();

        } catch (Exception e) {

            e.printStackTrace();

            AlertUtil.showError(
                    "Failed to remove direction."
            );
        }
    }
}
