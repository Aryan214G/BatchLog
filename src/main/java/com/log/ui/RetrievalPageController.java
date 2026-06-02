package com.log.ui;

import com.log.database.DBUtil;
import com.log.model.Batch;
import com.log.model.BatchTest;
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

    public void populateResults(List<Batch> results) {
        resultsContainer.getChildren().clear();
        selectedBatches.clear();

        if (results.isEmpty()) {
            Label empty = new Label("No results found");
            empty.setStyle("-fx-font-size: 16; -fx-text-fill: gray;");
            resultsContainer.getChildren().add(empty);
            return;
        }

        for (Batch batch : results) {
            resultsContainer.getChildren().add(createBatchCard(batch));
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

    private VBox createBatchCard(Batch batch) {
        CheckBox cb = new CheckBox();

        Button batchBtn = new Button("Batch: " + batch.getBatchId());
        batchBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(batchBtn, Priority.ALWAYS);
        batchBtn.setStyle("""
        -fx-background-color: #34495e;
        -fx-text-fill: white;
        -fx-font-size: 14;
        -fx-padding: 10;
        -fx-cursor: hand;
    """);
        batchBtn.setOnAction(e -> handleBatchClick(batch));

        // Checkbox adds all tests to selectedBatches for comparison
        for (BatchTest test : batch.getTests()) {
            cb.selectedProperty().addListener((obs, oldVal, selected) -> {
                if (selected) { if (!selectedBatches.contains(test)) selectedBatches.add(test); }
                else selectedBatches.remove(test);
            });
        }

        HBox row = new HBox(10, cb, batchBtn);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(row);
        VBox.setMargin(card, new Insets(0, 0, 8, 0));
        return card;
    }

    private void handleBatchClick(Batch batch) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/RetrievalResults.fxml")
            );
            Parent root = loader.load();

            RetrievalResultsController controller = loader.getController();
            controller.loadBatch(batch);  // pass the whole batch

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
        try (Connection connection = DBUtil.getConnection()) {
            SearchUtil searchUtil = new SearchUtil();
            List<Batch> results = searchUtil.searchBatches(
                    connection,
                    projectField.getText(),
                    productField.getText(),
                    BatchIDfield.getText(),
                    Dateoftestingfield.getText(),
                    Placeoftestingfield.getText()
            );
            populateResults(results);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}