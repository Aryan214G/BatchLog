package com.log.ui;

import com.log.database.DBUtil;
import com.log.model.BatchTest;
import com.log.service.PropertyService;
import com.log.util.AlertUtil;
import com.log.util.SearchUtil;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RetrievalPageController {

    @FXML private TextField projectField;
    @FXML private TextField productField;
    @FXML private TextField BatchIDfield;
    @FXML private TextField Dateoftestingfield;
    @FXML private TextField Placeoftestingfield;
    @FXML private VBox sidebar;
    @FXML private Button toggleBtn;
    @FXML private VBox resultsContainer;

    private boolean isOpen = true;
    private List<BatchTest> selectedBatches = new ArrayList<>();
    private PropertyService propertyService = new PropertyService();

    public void populateResults(List<BatchTest> results) {
        resultsContainer.getChildren().clear();
        selectedBatches.clear();

        if (results.isEmpty()) {
            Label empty = new Label("No results found");
            empty.setStyle("-fx-font-size: 16; -fx-text-fill: gray;");
            resultsContainer.getChildren().add(empty);
            return;
        }

        for (BatchTest batch : results) {

            // Checkbox on the left
            CheckBox cb = new CheckBox();
            cb.selectedProperty().addListener((obs, oldVal, selected) -> {
                if (selected) selectedBatches.add(batch);
                else selectedBatches.remove(batch);
            });

            // Original batch button
            Button btn = new Button(formatBatchText(batch));
            btn.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(btn, Priority.ALWAYS);
            btn.setStyle("""
                -fx-background-color: #34495e;
                -fx-text-fill: white;
                -fx-font-size: 14;
                -fx-padding: 10;
            """);
            btn.setOnAction(e -> handleBatchClick(batch));

            // Wrap checkbox + button in HBox
            HBox row = new HBox(10, cb, btn);
            row.setAlignment(Pos.CENTER_LEFT);

            resultsContainer.getChildren().add(row);
        }

        // Compare button at the bottom
        Button compareBtn = new Button("Compare Selected →");
        compareBtn.setStyle("""
            -fx-background-color: #5c3d9e;
            -fx-text-fill: white;
            -fx-font-size: 14;
            -fx-padding: 10 20 10 20;
            -fx-background-radius: 6;
            -fx-cursor: hand;
        """);
        compareBtn.setOnAction(e -> handleCompare());
        VBox.setMargin(compareBtn, new Insets(12, 0, 0, 0));
        resultsContainer.getChildren().add(compareBtn);
    }

    private String formatBatchText(BatchTest batch) {
        return "Batch: " + batch.getBatchCode() +
                " | Date: " + batch.getTestDate() +
                " | Site: " + batch.getTestSite();
    }

    private void handleBatchClick(BatchTest batch) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/RetrievalResults.fxml")
            );
            Parent root = loader.load();

            RetrievalResultsController controller = loader.getController();
            controller.loadBatch(batch);

            Stage stage = (Stage) resultsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCompare() {
        if (selectedBatches.isEmpty()) {
            AlertUtil.showWarning("Please select at least one batch to compare.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/BatchComparisonPage.fxml")
            );
            Parent root = loader.load();

            BatchComparisonController controller = loader.getController();
            controller.loadComparison(selectedBatches);

            Stage stage = (Stage) resultsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void toggleSidebar() {
        double targetX = isOpen ? -250 : 0;

        TranslateTransition sidebarAnim =
                new TranslateTransition(Duration.millis(250), sidebar);
        sidebarAnim.setToX(targetX);

        TranslateTransition buttonAnim =
                new TranslateTransition(Duration.millis(250), toggleBtn);
        buttonAnim.setToX(targetX);

        sidebarAnim.play();
        buttonAnim.play();

        isOpen = !isOpen;
    }

    @FXML
    private void handleSearch() {
        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        SearchUtil searchUtil = new SearchUtil();
        List<BatchTest> results = null;
        try {
            results = searchUtil.searchBatches(
                    connection,
                    projectField.getText(),
                    productField.getText(),
                    BatchIDfield.getText(),
                    Dateoftestingfield.getText(),
                    Placeoftestingfield.getText()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        populateResults(results);
    }
}