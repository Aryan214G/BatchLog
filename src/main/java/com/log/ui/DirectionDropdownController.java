package com.log.ui;

import com.log.model.Direction;
import com.log.service.DirectionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.List;

public class DirectionDropdownController {

    @FXML
    private ComboBox<String> directionCombo;

    private DirectionService directionService = new DirectionService();

    @FXML
    public void initialize() {
        List<Direction> directions = directionService.getAllDirections();
        List<String> directionValues = new ArrayList<>();
        for(Direction direction : directions){
            directionValues.add(direction.getDirVal());
        }
        directionCombo.setItems(
                FXCollections.observableArrayList(directionValues)
        );

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

