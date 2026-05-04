package com.log.ui;

import com.log.model.BatchTest;
import com.log.model.Property;
import com.log.service.PropertyService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class RetrievalResultsController {

    @FXML private Label batchTitleLabel;
    @FXML private GridPane propertiesGrid;

    private PropertyService propertyService = new PropertyService();

    public void loadBatch(BatchTest batch) {
        batchTitleLabel.setText(
                "Batch: " + batch.getBatchCode() +
                        " | Date: " + batch.getTestDate() +
                        " | Site: " + batch.getTestSite()
        );

        try {
            List<Property> properties = propertyService.getPropertiesByBatch(batch.getBatchCode());
            populateGrid(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateGrid(List<Property> properties) {
        // Header row
        propertiesGrid.add(makeHeader("Name"),       0, 0);
        propertiesGrid.add(makeHeader("Category"),   1, 0);
        propertiesGrid.add(makeHeader("Temp"),        2, 0);
        propertiesGrid.add(makeHeader("Direction"),  3, 0);
        propertiesGrid.add(makeHeader("Unit"),        4, 0);

        int row = 1;
        for (Property p : properties) {
            propertiesGrid.add(makeCell(p.getPropertyName()),                    0, row);
            propertiesGrid.add(makeCell(String.valueOf(p.getCategoryID())), 1, row);
            propertiesGrid.add(makeCell(String.valueOf(p.getTempID())),     2, row);
            propertiesGrid.add(makeCell(String.valueOf(p.getDirID())),      3, row);
            propertiesGrid.add(makeCell(String.valueOf(p.getUnitID())),     4, row);
            row++;
        }
    }

    private Label makeHeader(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("grid-header");
        return label;
    }

    private Label makeCell(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("grid-cell");
        return label;
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/RetrievalPage.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) propertiesGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}