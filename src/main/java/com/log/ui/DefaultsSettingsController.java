package com.log.ui;

import com.log.model.DefaultProperty;
import com.log.service.DefaultPropertyService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

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

    @FXML
    public void initialize() {

        loadCombos();

        propertyComboBox.setOnAction(event -> {

            DefaultProperty selected =
                    propertyComboBox.getValue();

            if(selected != null) {

                unitComboBox.setValue(
                        selected.getUnit()
                );

                fields.setText(
                        String.valueOf(selected.getRows())
                );
            }
        });
    }

    private void loadCombos() {

        List<DefaultProperty> properties =
                defaultPropertyService.getDefaults();

        propertyComboBox.getItems().addAll(properties);
        for(DefaultProperty prop : properties)
        {
            if(!unitComboBox.getItems().contains(prop.getUnit()))
            {
                unitComboBox.getItems().add(prop.getUnit());
            }
        }

    }

    @FXML
    private void saveChanges() {

        DefaultProperty selected =
                propertyComboBox.getValue();

        if(selected == null) {
            return;
        }

        int rows = Integer.parseInt(
                fields.getText()
        );

        selected.setRows(rows);

        defaultPropertyService.updateDefaultProperty(
                selected.getUnitId(),
                selected.getRows(),
                selected.getPropertyName()
        );

        System.out.println("Updated successfully.");
    }

    @FXML
    private void cancelChanges() {

        propertyComboBox.getSelectionModel().clearSelection();

        unitComboBox.getSelectionModel().clearSelection();

        fields.clear();
    }
}