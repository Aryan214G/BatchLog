package com.log.ui;

import com.log.core.StateManager;
import com.log.database.DBUtil;
import com.log.model.Batch;
import com.log.model.BatchTest;
import com.log.service.ProjectService;
import com.log.util.AlertUtil;
import com.log.util.SearchUtil;
import com.log.core.StateManager;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    @FXML private TextField SOPfield;
    @FXML private VBox sidebar;
    @FXML private Button toggleBtn;
    @FXML private VBox resultsContainer;

    private boolean isOpen = true;
    private List<BatchTest> selectedBatches = new ArrayList<>();
    private final ContextMenu projectSuggestions =
            new ContextMenu();

    private final ProjectService projectService =
            new ProjectService();

    @FXML
    public void initialize() {

        StateManager.clearAll();

        try (Connection conn = DBUtil.getConnection()) {

            List<String> projectNames =
                    projectService.getAllProjectNames(conn);

            projectField.textProperty().addListener(
                    (obs, oldValue, newValue) -> {
                        if (newValue == null ||
                                newValue.isBlank()) {
                            projectSuggestions.hide();
                            return;
                        }

                        List<String> matches = projectNames.stream().filter(name -> name.toLowerCase().contains(newValue.toLowerCase())).limit(5).toList();

                        if (matches.isEmpty()) {
                            projectSuggestions.hide();
                            return;
                        }

                        projectSuggestions.getItems().clear();

                        for (String match : matches) {

                            MenuItem item =
                                    new MenuItem(match);

                            item.setOnAction(e -> {

                                projectField.setText(match);

                                projectSuggestions.hide();
                            });

                            projectSuggestions.getItems().add(item);
                        }

                        if (!projectSuggestions.isShowing()) {

                            projectSuggestions.show(projectField, Side.BOTTOM, 0, 0);
                        }
                    }
            );

            projectField.focusedProperty().addListener(
                    (obs, oldVal, focused) -> {
                        if (!focused) {
                            projectSuggestions.hide();
                        }
                    }
            );

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

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

        // Show SOP from first test that has one
        String sopText = batch.getTests().stream()
                .map(BatchTest::getSOP)
                .filter(s -> s != null && !s.isBlank())
                .findFirst()
                .orElse(null);

        String label = "Batch: " + batch.getBatchId() +
                (sopText != null ? " | SOP: " + sopText : "");

        Button batchBtn = new Button(label);
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

    private VBox createProductTestCard(BatchTest test) {
        CheckBox cb = new CheckBox();
        cb.selectedProperty().addListener((obs, oldVal, selected) -> {
            if (selected) { if (!selectedBatches.contains(test)) selectedBatches.add(test); }
            else selectedBatches.remove(test);
        });

        String label = "Test ID: " + test.getTestId() +
                " | Date: " + (test.getTestDate() != null ? test.getTestDate() : "—") +
                " | Site: " + (test.getTestSite() != null ? test.getTestSite() : "—") +
                (test.getSOP() != null && !test.getSOP().isBlank() ? " | SOP: " + test.getSOP() : "");

        Button testBtn = new Button(label);
        testBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(testBtn, Priority.ALWAYS);
        testBtn.setStyle("""
        -fx-background-color: #2c5f4a;
        -fx-text-fill: white;
        -fx-font-size: 14;
        -fx-padding: 10;
        -fx-cursor: hand;
    """);
        testBtn.setOnAction(e -> handleProductTestClick(test));

        HBox row = new HBox(10, cb, testBtn);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(row);
        VBox.setMargin(card, new Insets(0, 0, 8, 0));
        return card;
    }

    private void handleProductTestClick(BatchTest test) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/RetrievalResults.fxml")
            );
            Parent root = loader.load();

            RetrievalResultsController controller = loader.getController();
            controller.loadBatch(test);  // existing loadBatch(BatchTest) handles this

            Stage stage = (Stage) resultsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearchBatches() {
        if (projectField.getText() == null ||
                projectField.getText().isBlank()) {

            AlertUtil.showWarning(
                    "Project Name is required."
            );
            return;
        }
        try (Connection connection = DBUtil.getConnection()) {
            SearchUtil searchUtil = new SearchUtil();
            List<Batch> results = searchUtil.searchBatches(
                    connection,
                    projectField.getText(),
                    productField.getText(),
                    BatchIDfield.getText(),
                    Dateoftestingfield.getText(),
                    Placeoftestingfield.getText(),
                    SOPfield.getText()
            );
            populateBatchResults(results);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleSearchProducts() {
        if (projectField.getText() == null ||
                projectField.getText().isBlank()) {

            AlertUtil.showWarning(
                    "Project Name is required."
            );
            return;
        }
        try (Connection connection = DBUtil.getConnection()) {
            SearchUtil searchUtil = new SearchUtil();
            List<BatchTest> results = searchUtil.searchProductTests(
                    connection,
                    projectField.getText(),
                    productField.getText(),
                    Dateoftestingfield.getText(),
                    Placeoftestingfield.getText(),
                    SOPfield.getText()
            );
            populateProductResults(results);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void populateBatchResults(List<Batch> results) {
        resultsContainer.getChildren().clear();
        selectedBatches.clear();

        if (results.isEmpty()) {
            Label empty = new Label("No batches found");
            empty.setStyle("-fx-font-size: 16; -fx-text-fill: gray;");
            resultsContainer.getChildren().add(empty);
            return;
        }

        for (Batch batch : results) {
            resultsContainer.getChildren().add(createBatchCard(batch));
        }

        addCompareButton();
    }

    private void populateProductResults(List<BatchTest> results) {
        resultsContainer.getChildren().clear();
        selectedBatches.clear();

        if (results.isEmpty()) {
            Label empty = new Label("No product tests found");
            empty.setStyle("-fx-font-size: 16; -fx-text-fill: gray;");
            resultsContainer.getChildren().add(empty);
            return;
        }

        for (BatchTest test : results) {
            resultsContainer.getChildren().add(createProductTestCard(test));
        }

        addCompareButton();
    }

    private void addCompareButton() {
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
}